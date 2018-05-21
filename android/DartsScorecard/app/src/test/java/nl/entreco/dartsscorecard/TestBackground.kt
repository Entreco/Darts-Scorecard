package nl.entreco.dartsscorecard

import nl.entreco.domain.common.executors.Background
import java.util.concurrent.Future
import java.util.concurrent.FutureTask


class TestBackground : Background {
    override fun post(runnable: Runnable): Future<*> {
        runnable.run()
        return FutureTask(runnable, 0)
    }
}