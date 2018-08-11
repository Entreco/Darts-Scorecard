package nl.entreco.dartsscorecard.streaming

import android.os.Handler
import android.os.Looper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.domain.streaming.ice.*
import nl.entreco.domain.streaming.p2p.ConnectToPeerUsecase
import nl.entreco.shared.log.Logger
import org.webrtc.*
import org.webrtc.IceCandidate
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class StreamingController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @ServiceScope private val connectToPeerUsecase: ConnectToPeerUsecase,
        @ServiceScope private val fetchIceServersUsecase: FetchIceServerUsecase,
        @ServiceScope private val listenForIceServersUsecase: ListenForIceCandidatesUsecase,
        @ServiceScope private val createOfferUsecase: CreateOfferUsecase,
        @ServiceScope private val peerConnectionFactory: PeerConnectionFactory,
        @ServiceScope private val videoCameraCapturer: CameraVideoCapturer?) {

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

    init {
        singleThreadExecutor.execute {
            initialize()
        }
    }

    private fun initialize() {

        if (videoCameraCapturer != null) {
            peerConnectionFactory.setVideoHwAccelerationOptions(eglBase.eglBaseContext, eglBase.eglBaseContext)
            videoSource = peerConnectionFactory.createVideoSource(videoCameraCapturer)
            localVideoTrack = peerConnectionFactory.createVideoTrack(counter.getAndIncrement().toString(), videoSource)
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
            //            listenForOffers() -> Only for the Receving end??
            initializeWebRtc(response.iceServers)
        }
    }


    private fun initializeWebRtc(servers: List<DscIceServer>) {
//        connectToPeerUsecase.go(ConnectToPeerRequest(servers), done = {}, fail = {})

        val iceServers = servers.map { PeerConnection.IceServer.builder(it.uri).createIceServer() }
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers,
                object : PeerConnection.Observer {
                    override fun onIceCandidate(p0: IceCandidate?) {
                        logger.w("PEER: onIceCandidate")
                        // TODO: send Ice Candidate to Firebase
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

                    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
                        logger.w("PEER: onIceCandidatesRemoved")
                        // TODO: Remove IceCandidates from Firebase
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
//        offeringPartyHandler = WebRtcOfferingPartyHandler(peerConnection, webRtcOfferingActionListener)
//        answeringPartyHandler = WebRtcAnsweringPartyHandler(peerConnection, getOfferAnswerConstraints(), webRtcAnsweringPartyListener)
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
}