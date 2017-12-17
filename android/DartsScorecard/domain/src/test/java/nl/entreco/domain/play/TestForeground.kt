package nl.entreco.domain.play

import nl.entreco.domain.executors.Foreground

/**
 * Created by Entreco on 12/12/2017.
 */
class TestForeground : Foreground {
    override fun post(runnable: Runnable) {
        runnable.run()
    }
}