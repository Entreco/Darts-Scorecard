package nl.entreco.dartsscorecard.streaming

import android.os.Handler
import android.os.Looper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack
import java.util.concurrent.Executors
import javax.inject.Inject

class StreamingController @Inject constructor() {

    private val eglBase = EglProvider.get()
    private var service: StreamingService? = null

    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private var localView: SurfaceViewRenderer? = null
    private var localVideoTrack: VideoTrack? = null

    fun attachService(service: StreamingService) {
        this.service = service
    }

    fun detachService() {
        this.service = null
    }

    fun detachViews() {
        // TODO
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

    var serviceListener: StreamingServiceListener? = null
}