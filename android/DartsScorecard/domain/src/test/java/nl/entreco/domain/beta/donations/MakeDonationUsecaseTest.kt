package nl.entreco.domain.beta.donations

import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MakeDonationUsecaseTest{


    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockFail : (Throwable) -> Unit
    @Mock private lateinit var mockDone : (MakePurchaseResponse) -> Unit
    @Mock private lateinit var mockBillingRepo : BillingRepository
    private lateinit var subject : MakeDonationUsecase

    @Test
    fun `it should report success if make donation succeeds`() {
        givenSubject()
        whenMakingDonationSucceeds("some payload")
        thenSuccessIsReported()
    }
    @Test
    fun `it should report error if fetch donation fails`() {
        givenSubject()
        whenMakingDonationFails(RuntimeException("play services not installed"))
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = MakeDonationUsecase(mockBillingRepo, bg, fg)
    }

    private fun whenMakingDonationSucceeds(payload: String) {
        val updateCaptor = argumentCaptor<(MakePurchaseResponse)->Unit>()
        subject.exec(MakeDonationRequest(mock()),mockDone, mockFail)
        verify(mockBillingRepo).donate(any(), updateCaptor.capture())
        updateCaptor.lastValue.invoke(MakePurchaseResponse.Consumed)
    }

    private fun whenMakingDonationFails(err: Throwable) {
        whenever(mockBillingRepo.donate(any(), any())).doThrow(err)
        subject.exec(MakeDonationRequest(Donation("ti", "de", "sk", "pr", 4, "EUR", 7990000)), mockDone, mockFail)
    }

    private fun thenSuccessIsReported() {
        verify(mockDone).invoke(any())
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }

}
