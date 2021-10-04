package nl.entreco.dartsscorecard.beta.donate

import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

/**
 * Created by entreco on 09/02/2018.
 */
class DonateViewModelTest {

    private val mockAnalytics: Analytics = mock()
    private lateinit var subject: DonateViewModel

    @Test
    fun `it should set loading(true) when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenLoadingIs(true)
    }

    private fun givenSubject() {
        subject = DonateViewModel(mockAnalytics)
    }

    private fun whenMakingDonation(sku: String) {
        val donation = Donation("donation", "desc", sku, "price", 4, "asf", 122222)
        subject.onDonate(donation)
    }

    private fun thenLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.loading.get())
    }
}
