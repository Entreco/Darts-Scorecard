package nl.entreco.domain.executors

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Created by Entreco on 12/12/2017.
 */
class BgExecutor : Background {
    private val bg: ExecutorService = Executors.newSingleThreadExecutor()
    override fun post(runnable: Runnable): Future<*> {
        return bg.submit { runnable.run() }
    }
}