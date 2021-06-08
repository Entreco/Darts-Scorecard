package nl.entreco.libcore.threading

import nl.entreco.libcore.threading.Background
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

/**
 * Created by Entreco on 12/12/2017.
 */
class TestBackground : Background {
    override fun post(runnable: Runnable): Future<*> {
        runnable.run()
        return FutureTask(runnable, 0)
    }
}