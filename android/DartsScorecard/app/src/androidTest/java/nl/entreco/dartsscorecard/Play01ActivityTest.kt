package nl.entreco.dartsscorecard

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Play01ActivityTest {
    @Test
    fun `it should have correct package name`() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("nl.entreco.dartsscorecard", appContext.packageName)
    }
}
