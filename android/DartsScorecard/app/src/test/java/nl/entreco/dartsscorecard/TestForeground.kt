package nl.entreco.dartsscorecard

import nl.entreco.shared.threading.Foreground


class TestForeground : Foreground {
    override fun post(runnable: Runnable) {
        runnable.run()
    }
}