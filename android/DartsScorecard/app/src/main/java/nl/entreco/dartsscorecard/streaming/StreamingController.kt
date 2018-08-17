package nl.entreco.dartsscorecard.streaming

import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.dartsscorecard.di.streaming.StreamingScope
import nl.entreco.dartsscorecard.streaming.constraints.OfferAnswerConstraints
import nl.entreco.dartsscorecard.streaming.constraints.WebRtcConstraints
import nl.entreco.dartsscorecard.streaming.constraints.addConstraints
import nl.entreco.domain.streaming.ice.*
import nl.entreco.shared.log.Logger
import org.webrtc.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class StreamingController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @StreamingScope private val singleThreadExecutor: ExecutorService,
        @StreamingScope private val webRtcController: WebRtcController,
        @ServiceScope private val listenForAnswersUsecase: ListenForAnswersUsecase,
        @ServiceScope private val createOfferUsecase: CreateOfferUsecase,
        @StreamingScope private val videoCameraCapturer: CameraVideoCapturer?) {

    private var service: StreamingService? = null
    var serviceListener: StreamingServiceListener? = null
    private var remoteUuid: String? = null

    private var finishedInitializing = AtomicBoolean(false)
    private var shouldCreateOffer = AtomicBoolean(false)

    private lateinit var offeringPartyHandler: WebRtcOfferingPartyHandler

    private val offerAnswerConstraints by lazy {
        WebRtcConstraints<OfferAnswerConstraints, Boolean>().apply {
            addMandatoryConstraint(OfferAnswerConstraints.OFFER_TO_RECEIVE_AUDIO, true)
            addMandatoryConstraint(OfferAnswerConstraints.OFFER_TO_RECEIVE_VIDEO, true)
        }
    }

    init {
        singleThreadExecutor.execute {
            initialize()
        }
    }

    private fun initialize() {
        webRtcController.initializeStreamer(videoCameraCapturer)
        if (videoCameraCapturer != null) {
            enableVideo(cameraEnabled, videoCameraCapturer)
        }
    }

    fun attachService(service: StreamingService) {
        this.service = service
        this.webRtcController.fetchIceServers(onServersRetrieved(), onCriticalError())
    }

    private fun onServersRetrieved(): (FetchIceServerResponse) -> Unit {
        return { response ->
            initializeWebRtcStreamer(response.iceServers,
                    object : WebRtcOfferingPartyHandler.Listener {
                        override fun onError(error: String) {
                            logger.e("WEBRTC: Error in offering party: $error")
                        }

                        override fun onOfferRemoteDescription(
                                localSessionDescription: SessionDescription) {
                            logger.w("WEBRTC: onOfferRemoteDescription $localSessionDescription")
                            listenForAnswers()
                            sendOffer(localSessionDescription)
                        }
                    })
        }
    }


    private fun initializeWebRtcStreamer(servers: List<DscIceServer>,
                                         listener: WebRtcOfferingPartyHandler.Listener) {

        val peerConnection = webRtcController.createStreamerConnection(servers,
                connectionChange = {
                    if (it == PeerConnection.IceConnectionState.DISCONNECTED) {
                        restart()
                    }
                    serviceListener?.connectionStateChange(it)
                })

        offeringPartyHandler = WebRtcOfferingPartyHandler(logger, peerConnection!!, listener)

        if (shouldCreateOffer.get()) createOffer()
        finishedInitializing.set(true)
    }

    private fun restart() {
        singleThreadExecutor.execute {
            offeringPartyHandler.createOffer(getOfferAnswerRestartConstraints())
        }
    }

    private fun sendOffer(localDescription: SessionDescription) {
        val recipientUuid = remoteUuid ?: throw IllegalArgumentException(
                "Remote uuid should be set first")
        val session = DscSessionDescription(localDescription.type.ordinal,
                localDescription.description)
        createOfferUsecase.go(CreateOfferRequest(recipientUuid, session), onCriticalError())
    }

    private fun createOffer() {
        singleThreadExecutor.execute {
            offeringPartyHandler.createOffer(getOfferAnswerConstraints())
        }
    }

    private fun onCriticalError(): (Throwable) -> Unit = { err ->
        serviceListener?.criticalWebRTCServiceException(err)
        service?.onStop()
    }

    fun detachService() {
        this.service = null
    }

    fun detachViews() {
        webRtcController.detachViews()
    }

    fun offerDevice(deviceUuid: String) {
        remoteUuid = deviceUuid
        webRtcController.listenForIceCandidate(deviceUuid, onCriticalError())

        if (finishedInitializing.get()) createOffer() else shouldCreateOffer.set(true)

    }

    fun attachLocalView(localView: SurfaceViewRenderer) {
        webRtcController.attachLocalView(localView)
    }

    /**
     * Safety net in case the owner of an object forgets to call its explicit termination method.
     * @see <a href="https://kotlinlang.org/docs/reference/java-interop.html#finalize">
     *     https://kotlinlang.org/docs/reference/java-interop.html#finalize</a>
     */
    @Suppress("unused", "ProtectedInFinal")
    protected fun finalize() {
        if (!singleThreadExecutor.isShutdown) {
            webRtcController.dispose()
            videoCameraCapturer?.dispose()
            singleThreadExecutor.shutdown()
        }
    }

    private val localVideoWidth: Int = 1280
    private val localVideoHeight: Int = 720
    private val localVideoFps: Int = 24
    private var cameraEnabled = true
        set(isEnabled) {
            field = isEnabled
            singleThreadExecutor.execute {
                videoCameraCapturer?.let { enableVideo(isEnabled, it) }
            }
        }

    private fun enableVideo(isEnabled: Boolean, videoCapturer: CameraVideoCapturer) {
        if (isEnabled) {
            videoCapturer.startCapture(localVideoWidth, localVideoHeight, localVideoFps)
        } else {
            videoCapturer.stopCapture()
        }
    }

    private fun listenForAnswers() {
        listenForAnswersUsecase.go({ response ->

            logger.w("WEBRTC: Listen for Answers $response")

            val type = SessionDescription.Type.values()[response.sessionType]
            val sessionDescription = SessionDescription(type, response.sessionDescription)
            handleRemoteAnswer(sessionDescription)

        }, onCriticalError())

    }

    /**
     * Handles received remote answer to our offer.
     */
    private fun handleRemoteAnswer(remoteSessionDescription: SessionDescription) {
        singleThreadExecutor.execute {
            offeringPartyHandler.handleRemoteAnswer(remoteSessionDescription)
        }
    }

    /**
     * Switches the camera to other if there is any available. By default front camera is used.
     * @param cameraSwitchHandler allows listening for switch camera event
     */
    @JvmOverloads
    fun switchCamera(cameraSwitchHandler: CameraVideoCapturer.CameraSwitchHandler? = null) {
        singleThreadExecutor.execute {
            videoCameraCapturer?.switchCamera(cameraSwitchHandler)
        }
    }

    private fun getOfferAnswerConstraints() = MediaConstraints().apply {
        addConstraints(offerAnswerConstraints)
    }

    private fun getOfferAnswerRestartConstraints() = getOfferAnswerConstraints().apply {
        mandatory.add(OfferAnswerConstraints.ICE_RESTART.toKeyValuePair(true))
    }
}