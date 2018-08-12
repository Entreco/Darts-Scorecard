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
import javax.inject.Inject

class ReceivingController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @ServiceScope private val listenForOfferUsecase: ListenForOfferUsecase,
        @ServiceScope private val sendAnswerUsecase: SendAnswerUsecase,
        @ServiceScope private val listenForIceServersUsecase: ListenForIceCandidatesUsecase,
        @ServiceScope private val sendIceCandidateUsecase: SendIceCandidateUsecase,
        @ServiceScope private val removeIceCandidateUsecase: RemoveIceCandidateUsecase,
        @ServiceScope private val fetchIceServersUsecase: FetchIceServerUsecase,
        @StreamingScope private val peerConnectionFactory: PeerConnectionFactory
) {

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    var serviceListener: ReceivingServiceListener? = null
    private var remoteUuid: String? = null

    private val eglBase = EglProvider.get()
    private var service: ReceiverService? = null
    private var peerConnection: PeerConnection? = null
    private val isPeerConnectionInitialized = AtomicBoolean(false)

    private var remoteView: SurfaceViewRenderer? = null
    private var remoteVideoTrack: VideoTrack? = null

    private lateinit var answeringPartyHandler: WebRtcAnsweringPartyHandler

    private val offerAnswerConstraints by lazy {
        WebRtcConstraints<OfferAnswerConstraints, Boolean>()
                .apply {
                    addMandatoryConstraint(
                            OfferAnswerConstraints.OFFER_TO_RECEIVE_AUDIO, true)
                    addMandatoryConstraint(
                            OfferAnswerConstraints.OFFER_TO_RECEIVE_VIDEO, true)
                }
    }

    fun attachService(service: ReceiverService) {
        this.service = service
        fetchIceServersUsecase.go(onServersRetrieved(), onCriticalError())
    }

    private fun onServersRetrieved(): (FetchIceServerResponse) -> Unit {
        return { response ->
            listenForOffers()
            initializeWebRtc(response.iceServers, object : WebRtcAnsweringPartyHandler.Listener {
                override fun onError(error: String) {
                    logger.e("Error in answering party: $error")
                }

                override fun onSuccess(localSessionDescription: SessionDescription) {
                    sendAnswer(localSessionDescription)
                }
            })
        }
    }

    private fun sendAnswer(localDescription: SessionDescription) {
        val description = DscSessionDescription(
                remoteUuid ?: throw IllegalArgumentException("Remote uuid should be set first"),
                localDescription.type.ordinal, localDescription.description)
        sendAnswerUsecase.go(SendAnswerRequest(description), onCriticalError())
    }

    private fun onCriticalError(): (Throwable) -> Unit = { err ->
        serviceListener?.criticalWebRTCServiceException(err)
        service?.onStop()
    }

    private fun initializeWebRtc(servers: List<DscIceServer>,
                                 listener: WebRtcAnsweringPartyHandler.Listener) {
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
                        if (p0?.videoTracks?.isNotEmpty() == true) {
                            onAddRemoteVideoStream(p0.videoTracks[0])
                        }
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
                        removeVideoStream()
                    }

                    override fun onRenegotiationNeeded() {
                        logger.w("PEER: onRenegotiationNeeded")
                    }

                    override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
                        logger.w("PEER: onAddTrack")
                    }

                })
        isPeerConnectionInitialized.set(true)

        answeringPartyHandler = WebRtcAnsweringPartyHandler(logger, peerConnection!!,
                getOfferAnswerConstraints(), listener)
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

    private fun listenForOffers() {
        listenForOfferUsecase.go({ response ->

            this.remoteUuid = response.remoteUuid
            listenForIceCandidate(response.remoteUuid)
            handleRemoteOffer(

                    // TODO: We get Firebase Int -> is it DescriptionType.OFFER?
                    SessionDescription(SessionDescription.Type.OFFER, response.sessionDescription))

        }, onCriticalError())
    }

    /**
     * Handles received remote offer. This will result in producing answer which will be returned in
     * [WebRtcAnsweringPartyListener] callback.
     */
    private fun handleRemoteOffer(remoteSessionDescription: SessionDescription) {
        singleThreadExecutor.execute {
            answeringPartyHandler.handleRemoteOffer(remoteSessionDescription)
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

    private fun onAddRemoteVideoStream(remoteVideoTrack: VideoTrack) {
        singleThreadExecutor.execute {
            this.remoteVideoTrack = remoteVideoTrack
            remoteView?.let {
                remoteVideoTrack.addSink(it)
            }
        }
    }

    private fun removeVideoStream() {
        singleThreadExecutor.execute {
            remoteVideoTrack = null
        }
    }

    /**
     * Attach [SurfaceViewRenderer] to webrtc client used for rendering remote view.
     */
    fun attachRemoteView(remoteView: SurfaceViewRenderer) {
        mainThreadHandler.post {
            remoteView.init(eglBase.eglBaseContext, null)
            this@ReceivingController.remoteView = remoteView
            singleThreadExecutor.execute {
                remoteVideoTrack?.addSink(remoteView)
            }
        }
    }

    private fun detachRemoteView() {
        mainThreadHandler.post {
            remoteView?.release()
            remoteView = null
        }
        singleThreadExecutor.execute {
            remoteVideoTrack?.removeSink(remoteView)
        }
    }

    fun detachService() {
        this.service = null
    }

    fun detachViews() {
        detachRemoteView()
    }

    private fun getOfferAnswerConstraints() = MediaConstraints().apply {
        addConstraints(offerAnswerConstraints)
    }
}