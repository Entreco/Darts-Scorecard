package nl.entreco.domain.beta.donations

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepository
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
    fun `it should report success if fetch donations succeeds`() {
        givenSubject()
        whenFetchinDonationsSucceeds()
        thenSuccessIsReported()
    }
    @Test
    fun `it should report error if fetch donations fails`() {
        givenSubject()
        whenFetchinDonationsThrows(RuntimeException("play services not installed"))
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = FetchDonationsUsecase(mockBillingRepo, bg, fg)
    }

    private fun whenFetchinDonationsSucceeds(vararg donations: Donation) {
        whenever(mockBillingRepo.fetchDonations()).thenReturn(listOf(*donations))
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchinDonationsThrows(err: Throwable) {
        whenever(mockBillingRepo.fetchDonations()).thenThrow(err)
        subject.exec(mockDone, mockFail)
    }

    private fun thenSuccessIsReported() {
        verify(mockDone).invoke(any())
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }
}