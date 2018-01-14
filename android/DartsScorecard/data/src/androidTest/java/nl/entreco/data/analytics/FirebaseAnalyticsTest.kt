package nl.entreco.data.analytics

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(AndroidJUnit4::class)
class FirebaseAnalyticsTest {

    private lateinit var subject: FirebaseAnalytics

    @Test
    fun `it should track events`() {
        givenSubject()
    }

    private fun givenSubject() {
        val context = InstrumentationRegistry.getContext()
        subject = FirebaseAnalytics(context)
    }
}