package nl.entreco.domain.beta.donations

import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import org.mockito.kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.domain.repository.BillingRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
class ConsumeDonationUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()

    private val mockBillingRepository: BillingRepo = mock()
    private val done: (ConsumeDonationResponse) -> Unit = mock()
    private val fail: (Throwable) -> Unit = mock()
    private lateinit var subject: ConsumeDonationUsecase

    private val purchaseData = "some token"

    @Before
    fun setUp() {
        subject = ConsumeDonationUsecase(mockBillingRepository, bg, fg)
    }

    @Test
    fun `it should notify callback of success`() {
        val doneCaptor = argumentCaptor<(MakePurchaseResponse)->Unit>()
        subject.exec(ConsumeDonationRequest(purchaseData, "sku", "orderid", true), done, fail)
        verify(mockBillingRepository.consume(any(), doneCaptor.capture()))
        doneCaptor.lastValue.invoke(MakePurchaseResponse.Consumed)
        verify(done).invoke(any())
    }

    @Test
    fun `it should notify callback of failure`() {
        whenever(mockBillingRepository.consume(any(), any())).doThrow(RuntimeException("Oh no you didn't"))
        subject.exec(ConsumeDonationRequest(purchaseData, "sku", "orderid", true), done, fail)
        verify(fail).invoke(any())
    }

    @Test
    fun `it should notify callback of failure when invalid json object`() {
        subject.exec(ConsumeDonationRequest("thiz iz no json dude", "sku", "id", true), done, fail)
        verify(fail).invoke(any())
    }

    @Test
    fun `it should not consume item when 'requiresConsumption is false'`() {
        subject.exec(ConsumeDonationRequest(purchaseData, "sku", "id", false), done, fail)
        verifyZeroInteractions(mockBillingRepository)
    }

    @Test
    fun `it should notify success when 'requiresConsumption is false'`() {
        subject.exec(ConsumeDonationRequest(purchaseData, "sku", "id", false), done, fail)
        verify(done).invoke(any())
    }

}
