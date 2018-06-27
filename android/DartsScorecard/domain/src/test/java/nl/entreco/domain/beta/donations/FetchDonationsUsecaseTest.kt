package nl.entreco.domain.beta.donations

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 09/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FetchDonationsUsecaseTest{

    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockFail : (Throwable) -> Unit
    @Mock private lateinit var mockDone : (FetchDonationsResponse) -> Unit
    @Mock private lateinit var mockBillingRepo : BillingRepository
    private lateinit var subject : FetchDonationsUsecase

    @Test
    fun `it should report success if fetch donations incl succeeds`() {
        givenSubject(false)
        whenFetchingDonationsInclSucceeds()
        thenSuccessIsReported(false)
    }

    @Test
    fun `it should report error if fetch donations incl fails`() {
        givenSubject(false)
        whenFetchingDonationsInclThrows(RuntimeException("play services not installed"))
        thenErrorIsReported()
    }

    @Test
    fun `it should report success if fetch donations excl succeeds`() {
        givenSubject(true)
        whenFetchingDonationsExclSucceeds()
        thenSuccessIsReported(true)
    }

    @Test
    fun `it should report error if fetch donations excl fails`() {
        givenSubject(true)
        whenFetchingDonationsExclThrows(RuntimeException("play services not installed"))
        thenErrorIsReported()
    }

    private fun givenSubject(purchasedBefore: Boolean) {
        whenever(mockBillingRepo.fetchPurchasedItems()).thenReturn(if(purchasedBefore) listOf("1", "2") else emptyList())
        subject = FetchDonationsUsecase(mockBillingRepo, bg, fg)
    }

    private fun whenFetchingDonationsInclSucceeds(vararg donations: Donation) {
        whenever(mockBillingRepo.fetchDonationsInclAds()).thenReturn(listOf(*donations))
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchingDonationsExclSucceeds(vararg donations: Donation) {
        whenever(mockBillingRepo.fetchDonationsExclAds()).thenReturn(listOf(*donations))
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchingDonationsInclThrows(err: Throwable) {
        whenever(mockBillingRepo.fetchDonationsInclAds()).thenThrow(err)
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchingDonationsExclThrows(err: Throwable) {
        whenever(mockBillingRepo.fetchDonationsExclAds()).thenThrow(err)
        subject.exec(mockDone, mockFail)
    }

    private fun thenSuccessIsReported(expected: Boolean) {
        val captor = argumentCaptor<FetchDonationsResponse>()
        verify(mockDone).invoke(captor.capture())
        assertEquals(expected, captor.lastValue.needToBeConsumed)
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }
}