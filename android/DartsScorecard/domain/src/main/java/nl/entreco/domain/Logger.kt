package nl.entreco.domain

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
    fun e(message: String, vararg args: String, error: Throwable)
}