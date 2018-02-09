package nl.entreco.dartsscorecard.beta.donate

import android.arch.lifecycle.Lifecycle
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.connect.ConnectToBillingUsecase
import nl.entreco.domain.beta.donations.ConsumeDonationUsecase
import nl.entreco.domain.beta.donations.FetchDonationsUsecase
import nl.entreco.domain.beta.donations.MakeDonationUsecase
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

    private fun givenSubject() {
        whenever(mockDonateCallback.lifeCycle()).thenReturn(mockLifecycle)
        subject = DonateViewModel(mockDonateCallback, mockConnectToBillingUsecase, mockFetchDonationsUsecase, mockMakeDonationsUsecase, mockConsumeDonationUsecase, mockAnalytics)
    }

    private fun itShouldAddObserver() {
        verify(mockDonateCallback).lifeCycle()
        verify(mockLifecycle).addObserver(subject)
    }

    private fun itShouldTrackViewingDonations() {
        verify(mockAnalytics).trackViewDonations()
    }
}