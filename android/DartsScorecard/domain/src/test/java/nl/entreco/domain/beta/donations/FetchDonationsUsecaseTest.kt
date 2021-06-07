package nl.entreco.domain.beta.donations

import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
        whenever(mockBillingRepo.fetchPurchasedItems()).doReturn(if(purchasedBefore) listOf("1", "2") else emptyList())
        subject = FetchDonationsUsecase(mockBillingRepo, bg, fg)
    }

    private fun whenFetchingDonationsInclSucceeds(vararg donations: Donation) {
        val doneCaptor = argumentCaptor<(List<Donation>)->Unit>()
        subject.exec(mockDone, mockFail)
        verify(mockBillingRepo).fetchDonationsInclAds(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(listOf(*donations))
    }

    private fun whenFetchingDonationsExclSucceeds(vararg donations: Donation) {
        val doneCaptor = argumentCaptor<(List<Donation>)->Unit>()
        subject.exec(mockDone, mockFail)
        verify(mockBillingRepo).fetchDonationsExclAds(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(listOf(*donations))
    }

    private fun whenFetchingDonationsInclThrows(err: Throwable) {
        whenever(mockBillingRepo.fetchDonationsInclAds(any(), any())).doThrow(err)
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchingDonationsExclThrows(err: Throwable) {
        whenever(mockBillingRepo.fetchDonationsExclAds(any(), any())).doThrow(err)
        subject.exec(mockDone, mockFail)
    }

    private fun thenSuccessIsReported(expected: Boolean) {
        val captor = argumentCaptor<FetchDonationsResponse>()
        verify(mockDone).invoke(captor.capture())
        assertEquals(expected, (captor.lastValue as FetchDonationsResponse.Ok).needToBeConsumed)
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }
}