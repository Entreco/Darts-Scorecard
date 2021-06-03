package nl.entreco.domain.common.executors

import nl.entreco.libcore.threading.Foreground

/**
 * Created by Entreco on 12/12/2017.
 */
class TestForeground : nl.entreco.libcore.threading.Foreground {
    override fun post(runnable: Runnable) {
        runnable.run()
    }
}