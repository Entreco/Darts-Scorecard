package nl.entreco.dartsscorecard

import nl.entreco.libcore.threading.Foreground


class TestForeground : nl.entreco.libcore.threading.Foreground {
    override fun post(runnable: Runnable) {
        runnable.run()
    }
}