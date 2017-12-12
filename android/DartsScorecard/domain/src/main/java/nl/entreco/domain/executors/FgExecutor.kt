package nl.entreco.domain.executors

import android.os.Handler
import android.os.Looper

/**
 * Created by Entreco on 12/12/2017.
 */
class FgExecutor : Foreground {
    private val fg: Handler = Handler(Looper.getMainLooper())
    override fun post(runnable: Runnable) {
        fg.post(runnable)
    }
}