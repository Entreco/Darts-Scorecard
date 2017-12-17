package nl.entreco.dartsscorecard

import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class DscLoggerTest {

    init {
        DscLogger.setEnabled(true)
    }

    private val subject = DscLogger("tag")

    @Test(expected = RuntimeException::class)
    fun `it should log 'd(msg)' (and hence crash)`() {
        subject.d("msg")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'd(msg, args)' (and hence crash)`() {
        subject.d("msg", "args")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'v(msg)' (and hence crash)`() {
        subject.v("msg")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'v(msg, args)' (and hence crash)`() {
        subject.v("msg", "args")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'i(msg)' (and hence crash)`() {
        subject.i("msg")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'i(msg, args)' (and hence crash)`() {
        subject.i("msg", "args")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'w(msg)' (and hence crash)`() {
        subject.w("msg")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'w(msg, args)' (and hence crash)`() {
        subject.w("msg", "args")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'e(msg)' (and hence crash)`() {
        subject.e("msg")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'e(msg, error)' (and hence crash)`() {
        subject.e("msg", Throwable("oops"))
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'e(msg, args)' (and hence crash)`() {
        subject.e("msg", "args")
    }

    @Test(expected = RuntimeException::class)
    fun `it should log 'e(msg, args, error)' (and hence crash)`() {
        subject.e("msg", Throwable("oh no"), "args")
    }

}