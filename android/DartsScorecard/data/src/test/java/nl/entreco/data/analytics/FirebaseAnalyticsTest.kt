package nl.entreco.data.analytics

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import nl.entreco.domain.beta.Donation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 27/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FirebaseAnalyticsTest {

    @Mock
    private lateinit var mockContext: Context
    private lateinit var subject: FirebaseAnalytics

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test(expected = NullPointerException::class)
    fun `it should track score`() {
        subject.trackScore("T20", 60)
    }

    @Test
    fun `it should parse price with ','`() {
        assertEquals(7.99, subject.formatMicros(7990000), 0.01)
    }

    @Test
    fun `it should parse price from SKU ()`() {
        assertEquals(0.0, subject.formatMicros(59), 0.01)
    }

    @Test
    fun `it should parse currency from SKU (€)`() {
        assertEquals(0.0, subject.formatMicros(59), 0.01)
    }

    @Test
    fun `it should have correct Scoring bundle`() {
        val bundle = subject.trackScoreBundle("120", 120)
        assertNotNull(bundle)
    }

    @Test
    fun `it should have correct Achievement bundle`() {
        val bundle = subject.trackAchievementBundle("achievement id")
        assertNotNull(bundle)
    }

    @Test
    fun `it should have correct ViewFeature bundle`() {
        val bundle = subject.trackViewFeatureBundle(1, mock { })
        assertNotNull(bundle)
    }

    @Test
    fun `it should have correct ViewFaq bundle`() {
        val bundle = subject.trackViewFaqBundle(mock { })
        assertNotNull(bundle)
    }

    @Test
    fun `it should have correct PurchaseFailed bundle`() {
        val bundle = subject.trackPurchaseFailedBundle("step", "prod id")
        assertNotNull(bundle)
    }

    @Test
    fun `it should have correct Donation bundle`() {
        val bundle = subject.toBundle(Donation("title", "desc", "sku", "12", 100, "EUR", 1000000))
        assertNotNull(bundle)
    }

    private fun givenSubject() {
        subject = FirebaseAnalytics(mockContext)
    }
}
