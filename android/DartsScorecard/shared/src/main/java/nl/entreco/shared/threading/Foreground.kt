package nl.entreco.shared.threading

/**
 * Created by Entreco on 12/12/2017.
 */
interface Foreground {
    fun post(runnable: Runnable)
}