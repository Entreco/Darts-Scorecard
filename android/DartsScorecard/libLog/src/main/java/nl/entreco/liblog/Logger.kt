package nl.entreco.liblog

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Entreco on 27/11/2017.
 */
interface Logger {
    fun d(message: String)
    fun d(message: String, vararg args: String)
    fun v(message: String)
    fun v(message: String, vararg args: String)
    fun i(message: String)
    fun i(message: String, vararg args: String)
    fun w(message: String)
    fun w(message: String, vararg args: String)
    fun e(message: String)
    fun e(message: String, error: Throwable)
    fun e(message: String, vararg args: String)
    fun e(message: String, error: Throwable, vararg args: String)

    companion object {
        fun default(tag: String): Logger {
            return AppLogger(tag)
        }

        val enabled : AtomicBoolean = AtomicBoolean(false)
    }
}