package nl.entreco.data.analytics

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 27/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FirebaseAnalyticsTest{

    @Mock private lateinit var mockContext: Context
    private lateinit var subject: FirebaseAnalytics

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun `it should parse price with ','`() {
        assertEquals(7.99, subject.formatMicros("7990000"), 0.01)
    }

    @Test
    fun `it should parse price from SKU ()`() {
        assertEquals(0.0, subject.formatMicros("€ 0,59"), 0.01)
    }

    @Test
    fun `it should parse currency from SKU (€)`() {
        assertEquals(0.0, subject.formatMicros("€ 0,59"), 0.01)
    }

    private fun givenSubject() {
        subject = FirebaseAnalytics(mockContext)
    }
}
