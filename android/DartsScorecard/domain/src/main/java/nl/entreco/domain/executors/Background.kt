package nl.entreco.domain.executors

import java.util.concurrent.Future

/**
 * Created by Entreco on 12/12/2017.
 */
interface Background {
    fun post(runnable: Runnable) : Future<*>?
}