package nl.entreco.dartsscorecard.streaming

import android.os.Handler
import android.os.Looper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.dartsscorecard.di.streaming.StreamingScope
import nl.entreco.dartsscorecard.streaming.constraints.OfferAnswerConstraints
import nl.entreco.dartsscorecard.streaming.constraints.WebRtcConstraints
import nl.entreco.dartsscorecard.streaming.constraints.addConstraints
import nl.entreco.domain.streaming.ice.*
import nl.entreco.domain.streaming.p2p.RemoveIceCandidateRequest
import nl.entreco.domain.streaming.p2p.RemoveIceCandidateUsecase
import nl.entreco.domain.streaming.p2p.SendIceCandidateRequest
import nl.entreco.domain.streaming.p2p.SendIceCandidateUsecase
import nl.entreco.shared.log.Logger
import org.webrtc.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class StreamingController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @ServiceScope private val listenForAnswersUsecase: ListenForAnswersUsecase,
        @ServiceScope private val sendIceCandidateUsecase: SendIceCandidateUsecase,
        @ServiceScope private val removeIceCandidateUsecase: RemoveIceCandidateUsecase,
        @ServiceScope private val fetchIceServersUsecase: FetchIceServerUsecase,
        @ServiceScope private val listenForIceServersUsecase: ListenForIceCandidatesUsecase,
        @ServiceScope private val createOfferUsecase: CreateOfferUsecase,
        @StreamingScope private val peerConnectionFactory: PeerConnectionFactory,
        @StreamingScope private val videoCameraCapturer: CameraVideoCapturer?) {

    private val eglBase = EglProvider.get()
    private var service: StreamingService? = null

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val counter = AtomicInteger(0)
    private fun getCounterStringValueAndIncrement() = counter.getAndIncrement().toString()

    private var videoSource: VideoSource? = null
    private var localView: SurfaceViewRenderer? = null
    private var localVideoTrack: VideoTrack? = null

    var serviceListener: StreamingServiceListener? = null
    private var remoteUuid: String? = null
    private var peerConnection: PeerConnection? = null
    private val isPeerConnectionInitialized = AtomicBoolean(false)

    private lateinit var offeringPartyHandler: WebRtcOfferingPartyHandler

    private val offerAnswerConstraints by lazy {
        WebRtcConstraints<OfferAnswerConstraints, Boolean>()
                .apply {
                    addMandatoryConstraint(
                            OfferAnswerConstraints.OFFER_TO_RECEIVE_AUDIO, true)
                    addMandatoryConstraint(
                            OfferAnswerConstraints.OFFER_TO_RECEIVE_VIDEO, true)
                }
    }

    init {
        singleThreadExecutor.execute {
            initialize()
        }
    }

    private fun initialize() {

        if (videoCameraCapturer != null) {
            peerConnectionFactory.setVideoHwAccelerationOptions(eglBase.eglBaseContext,
                    eglBase.eglBaseContext)
            videoSource = peerConnectionFactory.createVideoSource(videoCameraCapturer)
            localVideoTrack = peerConnectionFactory.createVideoTrack(
                    counter.getAndIncrement().toString(), videoSource)
            enableVideo(cameraEnabled, videoCameraCapturer)
        }

//        audioSource = peerConnectionFactory.createAudioSource(getAudioMediaConstraints())
//        localAudioTrack = peerConnectionFactory.createAudioTrack(getCounterStringValueAndIncrement(), audioSource)
    }

    fun attachService(service: StreamingService) {
        this.service = service
        fetchIceServersUsecase.go(onServersRetrieved(), onCriticalError())
    }

    private fun onServersRetrieved(): (FetchIceServerResponse) -> Unit {
        return { response ->
            initializeWebRtc(response.iceServers, object : WebRtcOfferingPartyHandler.Listener {
                override fun onError(error: String) {
                    logger.e("Error in offering party: $error")
                }

                override fun onOfferRemoteDescription(localSessionDescription: SessionDescription) {
                    listenForAnswers()
                    sendOffer(localSessionDescription)
                }
            })
            createOffer()
        }
    }


    private fun initializeWebRtc(servers: List<DscIceServer>,
                                 listener: WebRtcOfferingPartyHandler.Listener) {
        val iceServers = servers.map { PeerConnection.IceServer.builder(it.uri).createIceServer() }

        peerConnection = peerConnectionFactory.createPeerConnection(iceServers,
                object : PeerConnection.Observer {
                    override fun onIceCandidate(iceCandidate: IceCandidate?) {
                        logger.w("PEER: onIceCandidate")
                        sendIceCandidate(iceCandidate)
                    }

                    override fun onDataChannel(p0: DataChannel?) {
                        logger.w("PEER: onDataChannel")
                    }

                    override fun onIceConnectionReceivingChange(p0: Boolean) {
                        logger.w("PEER: onIceConnectionReceivingChange")
                    }

                    override fun onIceConnectionChange(
                            iceConnectionState: PeerConnection.IceConnectionState?) {
                        logger.w("PEER: onIceConnectionChange")
                        if (iceConnectionState == PeerConnection.IceConnectionState.DISCONNECTED) {
//                    TODO: webRtcClient.restart()
                        }
                        mainThreadHandler.post {
                            serviceListener?.connectionStateChange(iceConnectionState)
                        }
                    }

                    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
                        logger.w("PEER: onIceGatheringChange")
                    }

                    override fun onAddStream(p0: MediaStream?) {
                        logger.w("PEER: onAddStream")
                    }

                    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
                        logger.w("PEER: onSignalingChange")
                    }

                    override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
                        logger.w("PEER: onIceCandidatesRemoved")
                        removeIceCandidates(iceCandidates)
                    }

                    override fun onRemoveStream(p0: MediaStream?) {
                        logger.w("PEER: onRemoveStream")
                    }

                    override fun onRenegotiationNeeded() {
                        logger.w("PEER: onRenegotiationNeeded")
                    }

                    override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
                        logger.w("PEER: onAddTrack")
                    }

                })

        isPeerConnectionInitialized.set(true)

        val localMediaStream = peerConnectionFactory.createLocalMediaStream(
                getCounterStringValueAndIncrement())
//        localMediaStream.addTrack(localAudioTrack)
        localVideoTrack?.let { localMediaStream.addTrack(it) }

        peerConnection?.addStream(localMediaStream)
        offeringPartyHandler = WebRtcOfferingPartyHandler(logger, peerConnection!!, listener)
    }

    private fun sendIceCandidate(iceCandidate: IceCandidate?) {
        iceCandidate?.let { ice ->
            val candidate = DscIceCandidate(ice.sdpMid, ice.sdpMLineIndex, ice.sdp)
            sendIceCandidateUsecase.go(SendIceCandidateRequest(candidate),
                    done = { logger.i("Added IceCandidate $candidate") },
                    fail = { logger.w("Unable to add IceCandidate $candidate") })
        }
    }

    private fun removeIceCandidates(iceCandidates: Array<out IceCandidate>?) {
        iceCandidates?.let { ices ->
            val candidates = ices.map { DscIceCandidate(it.sdpMid, it.sdpMLineIndex, it.sdp) }
                    .toTypedArray()
            removeIceCandidateUsecase.go(RemoveIceCandidateRequest(candidates),
                    done = { logger.i("Removed IceCandidate $candidates") },
                    fail = { logger.w("Unable to Remove IceCandidates $candidates") })
        }
    }

    private fun sendOffer(localDescription: SessionDescription) {
        val description = DscSessionDescription(
                remoteUuid ?: throw IllegalArgumentException("Remote uuid should be set first"),
                localDescription.type.ordinal, localDescription.description)
        createOfferUsecase.go(CreateOfferRequest(description), {
            logger.d("description set")
        }, onCriticalError())
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
        detachLocalView()
    }

    fun offerDevice(deviceUuid: String) {
        remoteUuid = deviceUuid
        listenForIceCandidate(deviceUuid)

        // So, here we have a RACE condition -> we must have IceCandidates before we can make an offer
//        if (finishedInitializing) webRtcClient.createOffer() else shouldCreateOffer = true
//        createOfferUsecase.go(CreateOfferRequest(deviceUuid), {}, onCriticalError())
    }

    /**
     * Adds ice candidate from remote party to webrtc client
     */
    private fun addRemoteIceCandidate(iceCandidate: IceCandidate) {
        singleThreadExecutor.execute {
            peerConnection?.addIceCandidate(iceCandidate)
        }
    }

    /**
     * Removes ice candidates
     */
    private fun removeRemoteIceCandidate(iceCandidates: Array<IceCandidate>) {
        singleThreadExecutor.execute {
            peerConnection?.removeIceCandidates(iceCandidates)
        }
    }

    private fun listenForIceCandidate(deviceUuid: String) {
        listenForIceServersUsecase.go(ListenForIceCandidatesRequest(deviceUuid), { response ->

            val candidate = IceCandidate(response.candidate.sdpMid,
                    response.candidate.sdpMLineIndex, response.candidate.sdp)
            if (response.shouldAdd) {
                // Add DscIceCandidate to webRtc
                addRemoteIceCandidate(candidate)
            } else {
                // Remove DscIceCandidate from webRtc
                removeRemoteIceCandidate(arrayOf(candidate))
            }

        }, onCriticalError())
    }

    fun attachLocalView(localView: SurfaceViewRenderer) {
        mainThreadHandler.post {
            localView.init(eglBase.eglBaseContext, null)
            this@StreamingController.localView = localView
            singleThreadExecutor.execute {
                localVideoTrack?.addSink(localView)
            }
        }
    }

    fun detachLocalView() {
        singleThreadExecutor.execute {
            localVideoTrack?.removeSink(localView)
        }
        mainThreadHandler.post {
            localView?.release()
            localView = null
        }
    }


    private fun dispose() {
        singleThreadExecutor.execute {
            if (isPeerConnectionInitialized.get()) {
                peerConnection?.close()
                peerConnection?.dispose()
            }
            eglBase.release()
//            audioSource.dispose()
            videoCameraCapturer?.dispose()
            videoSource?.dispose()
            peerConnectionFactory.dispose()
        }
        singleThreadExecutor.shutdown()
    }

    /**
     * Safety net in case the owner of an object forgets to call its explicit termination method.
     * @see <a href="https://kotlinlang.org/docs/reference/java-interop.html#finalize">
     *     https://kotlinlang.org/docs/reference/java-interop.html#finalize</a>
     */
    @Suppress("unused", "ProtectedInFinal")
    protected fun finalize() {
        if (!singleThreadExecutor.isShutdown) {
            dispose()
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

            // TODO: We get Firebase Int -> is it DescriptionType.ANSWER
            val sessionDescription = SessionDescription(SessionDescription.Type.ANSWER,
                    response.sessionDescription)
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
}