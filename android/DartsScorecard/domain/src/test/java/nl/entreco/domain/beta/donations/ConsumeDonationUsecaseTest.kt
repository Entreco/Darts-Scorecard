package nl.entreco.domain.beta.donations

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 18/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ConsumeDonationUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockBillingRepository: BillingRepository
    @Mock private lateinit var done: (ConsumeDonationResponse) -> Unit
    @Mock private lateinit var fail: (Throwable) -> Unit
    private lateinit var subject: ConsumeDonationUsecase

    private val purchaseData = "{  \n" +
            "   \"purchaseToken\":\"aha\",\n" +
            "   \"orderId\":\"some order id\",\n" +
            "   \"sku\":\"id\"\n" +
            "}"

    @Before
    fun setUp() {
        subject = ConsumeDonationUsecase(mockBillingRepository, bg, fg)
    }

    @Test
    fun `it should notify callback of success`() {
        whenever(mockBillingRepository.consume(any())).thenReturn(1)
        subject.exec(ConsumeDonationRequest(purchaseData, "sig", true), done, fail)
        verify(done).invoke(any())
    }

    @Test
    fun `it should notify callback of failure`() {
        whenever(mockBillingRepository.consume(any())).thenThrow(RuntimeException("Oh no you didn't"))
        subject.exec(ConsumeDonationRequest(purchaseData, "sig", true), done, fail)
        verify(fail).invoke(any())
    }

    @Test
    fun `it should notify callback of failure when invalid json object`() {
        subject.exec(ConsumeDonationRequest("thiz iz no json dude", "sig", true), done, fail)
        verify(fail).invoke(any())
    }

    @Test
    fun `it should not consume item when 'requiresConsumption is false'`() {
        subject.exec(ConsumeDonationRequest(purchaseData, "sig", false), done, fail)
        verifyZeroInteractions(mockBillingRepository)
    }

    @Test
    fun `it should notify success when 'requiresConsumption is false'`() {
        subject.exec(ConsumeDonationRequest(purchaseData, "sig", false), done, fail)
        verify(done).invoke(any())
    }

}
