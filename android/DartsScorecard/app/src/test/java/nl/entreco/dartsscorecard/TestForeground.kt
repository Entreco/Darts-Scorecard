package nl.entreco.dartsscorecard

import nl.entreco.domain.common.executors.Foreground


class TestForeground : Foreground {
    override fun post(runnable: Runnable) {
        runnable.run()
    }
}