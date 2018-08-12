package nl.entreco.dartsscorecard.streaming

import android.os.Handler
import android.os.Looper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.dartsscorecard.di.streaming.StreamingScope
import nl.entreco.dartsscorecard.streaming.constraints.*
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
    private val counter = AtomicInteger(0)

    private val eglBase = EglProvider.get()
    private var service: ReceiverService? = null
    private var peerConnection: PeerConnection? = null
    private val isPeerConnectionInitialized = AtomicBoolean(false)

    private lateinit var audioSource: AudioSource
    private lateinit var localAudioTrack: AudioTrack

    private var remoteView: SurfaceViewRenderer? = null
    private var remoteVideoTrack: VideoTrack? = null

    private lateinit var answeringPartyHandler: WebRtcAnsweringPartyHandler

    private val audioBooleanConstraints by lazy {
        WebRtcConstraints<BooleanAudioConstraints, Boolean>().apply {
            addMandatoryConstraint(BooleanAudioConstraints.DISABLE_AUDIO_PROCESSING, true)
        }
    }

    private val audioIntegerConstraints by lazy {
        WebRtcConstraints<IntegerAudioConstraints, Int>()
    }

    private val offerAnswerConstraints by lazy {
        WebRtcConstraints<OfferAnswerConstraints, Boolean>().apply {
            addMandatoryConstraint(OfferAnswerConstraints.OFFER_TO_RECEIVE_AUDIO, true)
            addMandatoryConstraint(OfferAnswerConstraints.OFFER_TO_RECEIVE_VIDEO, true)
        }
    }

    private val peerConnectionConstraints by lazy {
        WebRtcConstraints<PeerConnectionConstraints, Boolean>().apply {
            addMandatoryConstraint(PeerConnectionConstraints.DTLS_SRTP_KEY_AGREEMENT_CONSTRAINT, true)
            addMandatoryConstraint(PeerConnectionConstraints.GOOG_CPU_OVERUSE_DETECTION, true)
        }
    }

    init {
        singleThreadExecutor.execute {
            initialize()
        }
    }

    private fun initialize() {
        audioSource = peerConnectionFactory.createAudioSource(getAudioMediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack(getCounterStringValueAndIncrement(), audioSource)
    }

    fun attachService(service: ReceiverService) {
        this.service = service
        fetchIceServersUsecase.go(onServersRetrieved(), onCriticalError())
    }

    private fun onServersRetrieved(): (FetchIceServerResponse) -> Unit {
        return { response ->
            listenForOffers()
            initializeWebRtcReceiver(response.iceServers, object : WebRtcAnsweringPartyHandler.Listener {
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
        val recipientUuid = remoteUuid ?: throw IllegalArgumentException("Remote uuid should be set first")
        val session = DscSessionDescription(localDescription.type.ordinal, localDescription.description)
        sendAnswerUsecase.go(SendAnswerRequest(recipientUuid, session), onCriticalError())
    }

    private fun onCriticalError(): (Throwable) -> Unit = { err ->
        serviceListener?.criticalWebRTCServiceException(err)
        service?.onStop()
    }

    private fun initializeWebRtcReceiver(servers: List<DscIceServer>,
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
                        logger.w("WEBRTC: onIceConnectionChange $iceConnectionState")
                        // NOTE: Restart is for StreamingController only
                        mainThreadHandler.post {
                            serviceListener?.connectionStateChange(iceConnectionState)
                        }
                    }

                    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
                        logger.w("PEER: onIceGatheringChange")
                    }

                    override fun onAddStream(p0: MediaStream?) {
                        logger.w("PEER: onAddStream $p0")
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

        answeringPartyHandler = WebRtcAnsweringPartyHandler(logger, peerConnection!!, getOfferAnswerConstraints(), listener)
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

            logger.w("WEBRTC: listenForNewOffersWithUuid $response")

            this.remoteUuid = response.senderUuid
            listenForIceCandidate(response.senderUuid)

            val type = SessionDescription.Type.values()[response.sessionType]
            val session = SessionDescription(type, response.sessionDescription)
            handleRemoteOffer(session)

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
            remoteVideoTrack.addSink(remoteView)
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

    fun dispose() {
        singleThreadExecutor.execute {
            if (isPeerConnectionInitialized.get()) {
                peerConnection?.close()
                peerConnection?.dispose()
            }
            eglBase.release()
            audioSource.dispose()
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

    private fun getCounterStringValueAndIncrement() = counter.getAndIncrement().toString()

    private fun getAudioMediaConstraints() = MediaConstraints().apply {
        addConstraints(audioBooleanConstraints, audioIntegerConstraints)
    }

//    private fun getPeerConnectionMediaConstraints() = MediaConstraints().apply {
//        addConstraints(peerConnectionConstraints)
//    }

    private fun getOfferAnswerConstraints() = MediaConstraints().apply {
        addConstraints(offerAnswerConstraints)
    }

//    private fun getOfferAnswerRestartConstraints() = getOfferAnswerConstraints().apply {
//        mandatory.add(OfferAnswerConstraints.ICE_RESTART.toKeyValuePair(true))
//    }
}