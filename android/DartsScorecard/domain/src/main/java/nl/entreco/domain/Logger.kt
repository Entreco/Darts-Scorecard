package nl.entreco.domain

/**
 * Created by Entreco on 27/11/2017.
 */
abstract class Logger {

    abstract fun d(message: String)
    abstract fun d(message: String, vararg args: String)
    abstract fun v(message: String)
    abstract fun v(message: String, vararg args: String)
    abstract fun i(message: String)
    abstract fun i(message: String, vararg args: String)
    abstract fun w(message: String)
    abstract fun w(message: String, vararg args: String)
    abstract fun e(message: String)
    abstract fun e(message: String, error: Throwable)
    abstract fun e(message: String, vararg args: String)
    abstract fun e(message: String, vararg args: String, error: Throwable)
}