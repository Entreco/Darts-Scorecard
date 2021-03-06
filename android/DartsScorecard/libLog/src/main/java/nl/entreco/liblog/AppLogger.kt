package nl.entreco.liblog

import android.util.Log

/**
 * Created by Entreco on 27/11/2017.
 */
internal class AppLogger(private val tag: String) : Logger {

    companion object {
        private var ENABLED = BuildConfig.DEBUG
        private var SEPARATOR = ", "
        fun setEnabled(enable: Boolean) {
            ENABLED = enable
        }
    }

    override fun d(message: String) {
        if (ENABLED) {
            Log.d(tag, message)
        }
    }

    override fun d(message: String, vararg args: String) {
        if (ENABLED) {
            Log.d(tag, withArgs(message, args))
        }
    }

    override fun v(message: String) {
        if (ENABLED) {
            Log.v(tag, message)
        }
    }

    override fun v(message: String, vararg args: String) {
        if (ENABLED) {
            Log.v(tag, withArgs(message, args))
        }
    }

    override fun i(message: String) {
        if (ENABLED) {
            Log.i(tag, message)
        }
    }

    override fun i(message: String, vararg args: String) {
        if (ENABLED) {
            Log.i(tag, withArgs(message, args))
        }
    }

    override fun w(message: String) {
        if (ENABLED) {
            Log.w(tag, message)
        }
    }

    override fun w(message: String, vararg args: String) {
        if (ENABLED) {
            Log.w(tag, withArgs(message, args))
        }
    }

    override fun e(message: String) {
        if (ENABLED) {
            Log.e(tag, message)
        }
    }

    override fun e(message: String, error: Throwable) {
        if (ENABLED) {
            Log.e(tag, message, error)
        }
    }

    override fun e(message: String, vararg args: String) {
        if (ENABLED) {
            Log.d(tag, withArgs(message, args))
        }
    }

    override fun e(message: String, error: Throwable, vararg args: String) {
        if (ENABLED) {
            Log.d(tag, withArgs(message, args), error)
        }
    }

    private fun withArgs(message: String, args: Array<out String>): String {
        return StringBuilder(message).append(
                SEPARATOR).apply {
            args.take(args.size - 1).forEach {
                append(it).append(
                        SEPARATOR)
            }.apply { append(args.last()) }
        }.toString()
    }
}