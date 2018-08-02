package nl.entreco.dartsscorecard

import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class DscNotLoggerTest {

    init {
        AppLogger.setEnabled(false)
    }

    private val subject = AppLogger("tag")

    @Test
    fun `it should NOT log 'd(msg)' (and hence NOT crash)`() {
        subject.d("msg")
    }

    @Test
    fun `it should NOT log 'd(msg, args)' (and hence NOT crash)`() {
        subject.d("msg", "args")
    }

    @Test
    fun `it should NOT log 'v(msg)' (and hence NOT crash)`() {
        subject.v("msg")
    }

    @Test
    fun `it should NOT log 'v(msg, args)' (and hence NOT crash)`() {
        subject.v("msg", "args")
    }

    @Test
    fun `it should NOT log 'i(msg)' (and hence NOT crash)`() {
        subject.i("msg")
    }

    @Test
    fun `it should NOT log 'i(msg, args)' (and hence NOT crash)`() {
        subject.i("msg", "args")
    }

    @Test
    fun `it should NOT log 'w(msg)' (and hence NOT crash)`() {
        subject.w("msg")
    }

    @Test
    fun `it should NOT log 'w(msg, args)' (and hence NOT crash)`() {
        subject.w("msg", "args")
    }

    @Test
    fun `it should log 'e(msg)' (and hence crash)`() {
        subject.e("msg")
    }

    @Test
    fun `it should log 'e(msg, error)' (and hence crash)`() {
        subject.e("msg", Throwable("oops"))
    }

    @Test
    fun `it should log 'e(msg, args)' (and hence crash)`() {
        subject.e("msg", "args")
    }

    @Test
    fun `it should log 'e(msg, args, error)' (and hence crash)`() {
        subject.e("msg", Throwable("oh no"), "args")
    }
}