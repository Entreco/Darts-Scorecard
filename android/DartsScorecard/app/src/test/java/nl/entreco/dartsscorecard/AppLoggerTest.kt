package nl.entreco.dartsscorecard

import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class AppLoggerTest {

    init {
        AppLogger.setEnabled(true)
    }

    private val subject = AppLogger("tag")

    @Test
    fun `it should log 'd(msg)'`() {
        subject.d("msg")
    }

    @Test
    fun `it should log 'd(msg, args)'`() {
        subject.d("msg", "args")
    }

    @Test
    fun `it should log 'v(msg)'`() {
        subject.v("msg")
    }

    @Test
    fun `it should log 'v(msg, args)'`() {
        subject.v("msg", "args")
    }

    @Test
    fun `it should log 'i(msg)'`() {
        subject.i("msg")
    }

    @Test
    fun `it should log 'i(msg, args)'`() {
        subject.i("msg", "args")
    }

    @Test
    fun `it should log 'w(msg)'`() {
        subject.w("msg")
    }

    @Test
    fun `it should log 'w(msg, args)'`() {
        subject.w("msg", "args")
    }

    @Test
    fun `it should log 'e(msg)'`() {
        subject.e("msg")
    }

    @Test
    fun `it should log 'e(msg, error)'`() {
        subject.e("msg", Throwable("oops"))
    }

    @Test
    fun `it should log 'e(msg, args)'`() {
        subject.e("msg", "args")
    }

    @Test
    fun `it should log 'e(msg, args, error)'`() {
        subject.e("msg", Throwable("oh no"), "args")
    }

}