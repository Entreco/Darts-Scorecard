package nl.entreco.domain

import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground

/**
 * Created by Entreco on 17/12/2017.
 */
abstract class BaseUsecase(private val bg: Background, private val fg: Foreground) {

    fun onBackground(f: () -> Unit, err: (Throwable) -> Unit) {
        bg.post(Runnable {
            try {
                f()
            } catch (oops: Throwable) {
                onErr(err, oops)
            }
        })
    }

    fun onUi(f: () -> Unit) {
        fg.post(Runnable { f() })
    }

    private fun onErr(f: (Throwable) -> Unit, oops: Throwable) {
        fg.post(Runnable { f(oops) })
    }
}