package nl.entreco.domain.ad

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FetchPurchasedItemsUsecaseTest {

    private var bg = TestBackground()
    private var fg = TestForeground()
    @Mock private lateinit var mockDone: (FetchPurchasedItemsResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockBillingRepo: BillingRepository
    private lateinit var subject: FetchPurchasedItemsUsecase

    private var givenPurchases = emptyList<String>()

    @Test
    fun `it should report ok(false) if purchases is not empty`() {
        givenPurchases("sku1", "sku2")
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(false)
    }

    @Test
    fun `it should report ok(true) if purchases is empty`() {
        givenPurchases()
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(true)
    }

    @Test
    fun `it should report failure if purchases cannot be retrieved`() {
        givenPurchases()
        givenSubject()
        whenFetchingFails()
        thenFailureIsReported()
    }

    private fun givenSubject() {
        subject = FetchPurchasedItemsUsecase(mockBillingRepo, bg, fg)
    }

    private fun givenPurchases(vararg skus: String) {
        givenPurchases = skus.map { it }
    }

    private fun whenFetchingSucceeds() {
        whenever(mockBillingRepo.fetchPurchasedItems()).thenReturn(givenPurchases)
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchingFails() {
        whenever(mockBillingRepo.fetchPurchasedItems()).thenThrow(RuntimeException("do'h"))
        subject.exec(mockDone, mockFail)
    }

    private fun thenOkIsReported(expected: Boolean) {
        verify(mockDone).invoke(FetchPurchasedItemsResponse(expected))
    }

    private fun thenFailureIsReported() {
        verify(mockFail).invoke(any())
    }
}
