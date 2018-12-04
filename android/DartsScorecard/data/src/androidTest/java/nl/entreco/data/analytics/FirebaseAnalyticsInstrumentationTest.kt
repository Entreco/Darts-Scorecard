package nl.entreco.data.analytics

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import nl.entreco.domain.beta.Feature
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(AndroidJUnit4::class)
class FirebaseAnalyticsInstrumentationTest {

    private lateinit var subject: FirebaseAnalytics

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun itShouldTrackScore() {
        subject.trackScore("T20", 60)
    }

    @Test
    fun itShouldTrackAchievement() {
        subject.trackAchievement("achievement id")
    }

    @Test
    fun itShouldTrackViewFeature() {
        subject.trackViewFeature(Feature("ref", "ti", "d", "i", "u", 12, 2, "video"), amount)
    }

    private fun givenSubject() {
        val context = InstrumentationRegistry.getContext()
        subject = FirebaseAnalytics(context)
    }
}
