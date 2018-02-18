package nl.entreco.dartsscorecard.beta.donate

import android.arch.lifecycle.Lifecycle
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.connect.ConnectToBillingUsecase
import nl.entreco.domain.beta.donations.ConsumeDonationUsecase
import nl.entreco.domain.beta.donations.FetchDonationsUsecase
import nl.entreco.domain.beta.donations.MakeDonationResponse
import nl.entreco.domain.beta.donations.MakeDonationUsecase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 09/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DonateViewModelTest {

    @Mock private lateinit var mockLifecycle : Lifecycle
    @Mock private lateinit var mockDonateCallback : DonateCallback
    @Mock private lateinit var mockConnectToBillingUsecase : ConnectToBillingUsecase
    @Mock private lateinit var mockFetchDonationsUsecase : FetchDonationsUsecase
    @Mock private lateinit var mockMakeDonationsUsecase : MakeDonationUsecase
    @Mock private lateinit var mockConsumeDonationUsecase : ConsumeDonationUsecase
    @Mock private lateinit var mockAnalytics : Analytics
    private lateinit var subject : DonateViewModel

    private val doneCaptor = argumentCaptor<(MakeDonationResponse)->Unit>()
    private val failCaptor = argumentCaptor<(Throwable)->Unit>()

    @Test
    fun `it should fetch donations`() {
        givenSubject()
        itShouldAddObserver()
    }

    @Test
    fun `it should track viewing donations`() {
        givenSubject()
        itShouldTrackViewingDonations()
    }

    @Test
    fun `it should set loading(true) when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenLoadingIs(true)
    }

    @Test
    fun `it should store SKU when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenProductIdIs("sku")
    }

    @Test
    fun `it should execute usecase when making donations`() {
        givenSubject()
        whenMakingDonation("sku")
        thenUsecaseIsExecuted()
    }

    @Test
    fun `it should notify callback when making donations succeeds`() {
        givenSubject()
        whenMakingDonationSucceeds("sku")
        thenCallbackIsNotified()
    }

    @Test
    fun `it should set loading(false) when making donations fails`() {
        givenSubject()
        whenMakingDonationFails(RuntimeException("Alas"))
        thenLoadingIs(false)
    }

    @Test
    fun `it should clear ProductId when making donations fails`() {
        givenSubject()
        whenMakingDonationFails(RuntimeException("Alas"))
        thenProductIdIs("")
    }

    private fun givenSubject() {
        whenever(mockDonateCallback.lifeCycle()).thenReturn(mockLifecycle)
        subject = DonateViewModel(mockDonateCallback, mockConnectToBillingUsecase, mockFetchDonationsUsecase, mockMakeDonationsUsecase, mockConsumeDonationUsecase, mockAnalytics)
    }

    private fun whenMakingDonation(sku: String) {
        val donation = Donation("donation", "desc", sku, "price", 4)
        subject.onDonate(donation)
    }

    private fun whenMakingDonationSucceeds(sku: String) {
        whenMakingDonation(sku)
        verify(mockMakeDonationsUsecase).exec(any(), doneCaptor.capture(), any())
        val response = mock<MakeDonationResponse>()
        doneCaptor.lastValue.invoke(response)
    }

    private fun whenMakingDonationFails(err: Throwable) {
        whenMakingDonation("some sku")
        verify(mockMakeDonationsUsecase).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun itShouldAddObserver() {
        verify(mockDonateCallback).lifeCycle()
        verify(mockLifecycle).addObserver(subject)
    }

    private fun itShouldTrackViewingDonations() {
        verify(mockAnalytics).trackViewDonations()
    }

    private fun thenLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.loading.get())
    }

    private fun thenProductIdIs(expected: String) {
        assertEquals(expected, subject.productId)
    }

    private fun thenUsecaseIsExecuted() {
        verify(mockMakeDonationsUsecase).exec(any(), any(), any())
    }

    private fun thenCallbackIsNotified() {
        verify(mockDonateCallback).makeDonation(any())
    }
}
