package nl.entreco.domain.common.executors

import android.os.Handler
import android.os.Looper

/**
 * Created by Entreco on 12/12/2017.
 */
class FgExecutor(private val fg: Handler = Handler(Looper.getMainLooper())) : Foreground {

    override fun post(runnable: Runnable) {
        fg.post(runnable)
    }
}