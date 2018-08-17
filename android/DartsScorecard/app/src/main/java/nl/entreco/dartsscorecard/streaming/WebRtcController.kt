package nl.entreco.dartsscorecard.streaming

import android.os.Handler
import android.os.Looper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.dartsscorecard.di.streaming.StreamingScope
import nl.entreco.dartsscorecard.streaming.constraints.BooleanAudioConstraints
import nl.entreco.dartsscorecard.streaming.constraints.IntegerAudioConstraints
import nl.entreco.dartsscorecard.streaming.constraints.WebRtcConstraints
import nl.entreco.dartsscorecard.streaming.constraints.addConstraints
import nl.entreco.domain.streaming.ice.*
import nl.entreco.domain.streaming.p2p.RemoveIceCandidateRequest
import nl.entreco.domain.streaming.p2p.RemoveIceCandidateUsecase
import nl.entreco.domain.streaming.p2p.SendIceCandidateRequest
import nl.entreco.domain.streaming.p2p.SendIceCandidateUsecase
import nl.entreco.shared.log.Logger
import org.webrtc.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class WebRtcController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @StreamingScope private val singleThreadExecutor: ExecutorService,
        @ServiceScope private val sendIceCandidateUsecase: SendIceCandidateUsecase,
        @ServiceScope private val removeIceCandidateUsecase: RemoveIceCandidateUsecase,
        @ServiceScope private val fetchIceServersUsecase: FetchIceServerUsecase,
        @ServiceScope private val listenForIceServersUsecase: ListenForIceCandidatesUsecase,
        @StreamingScope private val peerConnectionFactory: PeerConnectionFactory
) {

    private val counter = AtomicInteger(0)
    private val eglBase = EglProvider.get()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val isPeerConnectionInitialized = AtomicBoolean(false)

    private var peerConnection: PeerConnection? = null
    private var localView: SurfaceViewRenderer? = null

    private lateinit var audioSource: AudioSource
    private lateinit var localAudioTrack: AudioTrack

    private var videoSource: VideoSource? = null
    private var localVideoTrack: VideoTrack? = null

    private var remoteView: SurfaceViewRenderer? = null
    private var remoteVideoTrack: VideoTrack? = null

    fun initializeReceiver() {
        audioSource = peerConnectionFactory.createAudioSource(getAudioMediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack(
                getCounterStringValueAndIncrement(), audioSource)
    }

    fun initializeStreamer(videoCameraCapturer: CameraVideoCapturer?) {
        if (videoCameraCapturer != null) {
            peerConnectionFactory.setVideoHwAccelerationOptions(eglBase.eglBaseContext,
                    eglBase.eglBaseContext)
            videoSource = peerConnectionFactory.createVideoSource(videoCameraCapturer)
            localVideoTrack = peerConnectionFactory.createVideoTrack(
                    counter.getAndIncrement().toString(), videoSource)
        }

        audioSource = peerConnectionFactory.createAudioSource(getAudioMediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack(
                getCounterStringValueAndIncrement(), audioSource)
    }

    private fun getCounterStringValueAndIncrement() = counter.getAndIncrement().toString()

    private val audioBooleanConstraints by lazy {
        WebRtcConstraints<BooleanAudioConstraints, Boolean>().apply {
            addMandatoryConstraint(BooleanAudioConstraints.DISABLE_AUDIO_PROCESSING, true)
        }
    }

    private val audioIntegerConstraints by lazy {
        WebRtcConstraints<IntegerAudioConstraints, Int>()
    }

    private fun getAudioMediaConstraints() = MediaConstraints().apply {
        addConstraints(audioBooleanConstraints, audioIntegerConstraints)
    }

    fun fetchIceServers(done: (FetchIceServerResponse) -> Unit, fail: (Throwable) -> Unit) {
        fetchIceServersUsecase.go(done, fail)
    }

    fun createReceiverConnection(servers: List<DscIceServer>,
                                 connectionChange: (PeerConnection.IceConnectionState?) -> Unit): PeerConnection? {
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
                            connectionChange(iceConnectionState)
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
        return peerConnection
    }

    fun createStreamerConnection(servers: List<DscIceServer>,
                                 connectionChange: (PeerConnection.IceConnectionState?) -> Unit): PeerConnection? {
        peerConnection = createReceiverConnection(servers, connectionChange)

        val localMediaStream = peerConnectionFactory.createLocalMediaStream(
                getCounterStringValueAndIncrement())
        localMediaStream.addTrack(localAudioTrack)
        localVideoTrack?.let { localMediaStream.addTrack(it) }

        peerConnection?.addStream(localMediaStream)

        return peerConnection
    }

    fun listenForIceCandidate(deviceUuid: String, fail: (Throwable) -> Unit) {
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

        }, fail)
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

    fun attachLocalView(localView: SurfaceViewRenderer) {
        mainThreadHandler.post {
            localView.init(eglBase.eglBaseContext, null)
            this@WebRtcController.localView = localView
            singleThreadExecutor.execute {
                localVideoTrack?.addSink(localView)
            }
        }
    }

    private fun detachLocalView() {
        singleThreadExecutor.execute {
            localVideoTrack?.removeSink(localView)
        }
        mainThreadHandler.post {
            localView?.release()
            localView = null
        }
    }

    fun attachRemoteView(remoteView: SurfaceViewRenderer) {
        mainThreadHandler.post {
            remoteView.init(eglBase.eglBaseContext, null)
            this@WebRtcController.remoteView = remoteView
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

    fun detachViews() {
        detachLocalView()
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
            videoSource?.dispose()
            peerConnectionFactory.dispose()
        }
        singleThreadExecutor.shutdown()
    }


}