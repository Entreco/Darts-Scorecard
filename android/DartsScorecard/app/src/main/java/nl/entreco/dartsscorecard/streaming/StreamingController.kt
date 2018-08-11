package nl.entreco.dartsscorecard.streaming

import android.os.Handler
import android.os.Looper
import nl.entreco.dartsscorecard.di.application.ApplicationScope
import nl.entreco.dartsscorecard.di.service.ServiceScope
import nl.entreco.shared.log.Logger
import org.webrtc.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class StreamingController @Inject constructor(
        @ApplicationScope private val logger: Logger,
        @ServiceScope private val peerConnectionFactory: PeerConnectionFactory,
        @ServiceScope private val videoCameraCapturer: CameraVideoCapturer?) {

    private val eglBase = EglProvider.get()
    private var service: StreamingService? = null

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val counter = AtomicInteger(0)
    private var videoSource: VideoSource? = null
    private var localView: SurfaceViewRenderer? = null
    private var localVideoTrack: VideoTrack? = null

    var serviceListener: StreamingServiceListener? = null

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
    }

    fun detachService() {
        this.service = null
    }

    fun detachViews() {
        detachLocalView()
//        detachRemoteView()
    }

    fun attachLocalView(localView: SurfaceViewRenderer) {
        mainThreadHandler.post {
            localView.init(eglBase.eglBaseContext, null)
            this@StreamingController.localView = localView
            singleThreadExecutor.execute {
                localVideoTrack?.addSink(localView)
//                localVideoTrack?.addRenderer()
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


    fun dispose() {
        singleThreadExecutor.execute {
            //            if (isPeerConnectionInitialized) {
//                peerConnection.close()
//                peerConnection.dispose()
//            }
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