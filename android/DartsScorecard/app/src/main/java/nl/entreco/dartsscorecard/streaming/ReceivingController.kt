package nl.entreco.dartsscorecard.streaming

import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.dartsscorecard.di.streaming.StreamingScope
import nl.entreco.dartsscorecard.streaming.constraints.OfferAnswerConstraints
import nl.entreco.dartsscorecard.streaming.constraints.WebRtcConstraints
import nl.entreco.dartsscorecard.streaming.constraints.addConstraints
import nl.entreco.domain.streaming.ice.*
import nl.entreco.shared.log.Logger
import org.webrtc.MediaConstraints
import org.webrtc.SessionDescription
import org.webrtc.SurfaceViewRenderer
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class ReceivingController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @StreamingScope private val singleThreadExecutor: ExecutorService,
        @StreamingScope private val webRtcController: WebRtcController,
        @ServiceScope private val listenForOfferUsecase: ListenForOfferUsecase,
        @ServiceScope private val sendAnswerUsecase: SendAnswerUsecase
) {

    private var service: ReceiverService? = null
    var serviceListener: ReceivingServiceListener? = null
    private var remoteUuid: String? = null

    private lateinit var answeringPartyHandler: WebRtcAnsweringPartyHandler

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
        webRtcController.initializeReceiver()
    }

    fun attachService(service: ReceiverService) {
        this.service = service
        this.webRtcController.fetchIceServers(onServersRetrieved(), onCriticalError())
    }

    private fun onServersRetrieved(): (FetchIceServerResponse) -> Unit {
        return { response ->
            listenForOffers()
            initializeWebRtcReceiver(response.iceServers,
                    object : WebRtcAnsweringPartyHandler.Listener {
                        override fun onError(error: String) {
                            logger.e("WEBRTC: Error in answering party: $error")
                        }

                        override fun onSuccess(localSessionDescription: SessionDescription) {
                            logger.w("WEBRTC: onSuccess $localSessionDescription")
                            sendAnswer(localSessionDescription)
                        }
                    })
        }
    }

    private fun sendAnswer(localDescription: SessionDescription) {
        val recipientUuid = remoteUuid ?: throw IllegalArgumentException(
                "Remote uuid should be set first")
        val session = DscSessionDescription(localDescription.type.ordinal,
                localDescription.description)
        sendAnswerUsecase.go(SendAnswerRequest(recipientUuid, session), onCriticalError())
    }

    private fun onCriticalError(): (Throwable) -> Unit = { err ->
        serviceListener?.criticalWebRTCServiceException(err)
        service?.onStop()
    }

    private fun initializeWebRtcReceiver(servers: List<DscIceServer>,
                                         listener: WebRtcAnsweringPartyHandler.Listener) {
        val peerConnection = webRtcController.createReceiverConnection(servers,
                connectionChange = {
                    serviceListener?.connectionStateChange(it)
                })

        answeringPartyHandler = WebRtcAnsweringPartyHandler(logger, peerConnection!!,
                getOfferAnswerConstraints(), listener)
    }

    private fun listenForOffers() {
        listenForOfferUsecase.go({ response ->

            logger.w("WEBRTC: listenForNewOffersWithUuid $response")

            this.remoteUuid = response.senderUuid
            webRtcController.listenForIceCandidate(response.senderUuid, onCriticalError())

            val type = SessionDescription.Type.values()[response.sessionType]
            val session = SessionDescription(type, response.sessionDescription)
            handleRemoteOffer(session)

        }, onCriticalError())
    }

    private fun handleRemoteOffer(remoteSessionDescription: SessionDescription) {
        singleThreadExecutor.execute {
            answeringPartyHandler.handleRemoteOffer(remoteSessionDescription)
        }
    }

    /**
     * Attach [SurfaceViewRenderer] to webrtc client used for rendering remote view.
     */
    fun attachRemoteView(remoteView: SurfaceViewRenderer) {
        webRtcController.attachRemoteView(remoteView)
    }

    fun detachService() {
        this.service = null
    }

    fun detachViews() {
        webRtcController.detachViews()
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
            singleThreadExecutor.shutdown()
        }
    }

    private fun getOfferAnswerConstraints() = MediaConstraints().apply {
        addConstraints(offerAnswerConstraints)
    }
}