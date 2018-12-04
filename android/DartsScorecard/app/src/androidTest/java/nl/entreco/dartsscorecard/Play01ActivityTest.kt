package nl.entreco.dartsscorecard

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Play01ActivityTest {
    @Test
    fun itShouldHaveCorrectPackageName() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("nl.entreco.dartsscorecard", appContext.packageName)
    }
}
