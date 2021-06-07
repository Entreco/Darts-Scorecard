package nl.entreco.dartsscorecard.beta.donate

import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 09/02/2018.
 */
class DonateViewModelTest {

    private val mockAnalytics: Analytics = mock()
    private lateinit var subject: DonateViewModel

    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()


    @Test
    fun `it should set loading(true) when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenLoadingIs(true)
    }

    @Test
    fun `it should set loading(false) when making donations fails`() {
        givenSubject()
        whenMakingDonationFails(RuntimeException("Alas"))
        thenLoadingIs(false)
    }

    @Test
    fun `it should set loading(false) when make donation fails`() {
        givenSubject()
        whenMakingDonationFails()
        thenLoadingIs(false)
    }

    @Test
    fun `it should track Purchase Failed when make donation fails`() {
        givenSubject()
        whenMakingDonationFails()
        thenPurchaseFailedIstracked("Donation ActivityResult failed")
    }


    private fun givenSubject() {
        subject = DonateViewModel(mockAnalytics)
    }

    private fun givenDonation(productId: String) {
        subject.donations.add(Donation("title", "desc", productId, "price", 3, "EUR", 7900000))
    }

    private fun whenMakingDonation(sku: String) {
        val donation = Donation("donation", "desc", sku, "price", 4, "asf", 122222)
        subject.onDonate(donation)
    }

    private fun whenMakingDonationFails(err: Throwable = IllegalStateException("doh")) {
        whenMakingDonation("some sku")
        failCaptor.lastValue.invoke(err)
    }

    private fun thenLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.loading.get())
    }

    private fun thenPurchaseFailedIstracked(step: String) {
        verify(mockAnalytics).trackPurchaseFailed(any(), eq(step))
    }
}
