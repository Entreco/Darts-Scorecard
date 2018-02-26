package nl.entreco.data.analytics

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import nl.entreco.domain.beta.Feature
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(AndroidJUnit4::class)
class FirebaseAnalyticsTest {

    private lateinit var subject: FirebaseAnalytics

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun `it should parse price with ,`() {
        assertEquals(12.50, subject.parsePrice("12,50"), 0.01)
    }

    @Test
    fun `it should track score`() {
        subject.trackScore("T20", 60)
    }

    @Test
    fun `it should track achievement`() {
        subject.trackAchievement("achievement id")
    }

    @Test
    fun `it should track view feature`() {
        subject.trackViewFeature(Feature("ref", "ti", "d", "i", "u", 12, 2))
    }

    private fun givenSubject() {
        val context = InstrumentationRegistry.getContext()
        subject = FirebaseAnalytics(context)
    }
}
