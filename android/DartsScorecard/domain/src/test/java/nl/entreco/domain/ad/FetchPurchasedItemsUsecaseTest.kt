package nl.entreco.domain.ad

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.domain.repository.GameRepository
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
    @Mock private lateinit var mockGameRepo: GameRepository
    @Mock private lateinit var mockBillingRepo: BillingRepo
    private lateinit var subject: FetchPurchasedItemsUsecase

    private var givenPurchases = emptyList<String>()
    private var givenNumberOfGames = 0

    @Test
    fun `it should report ok(false) if purchases is not empty and gameCount gt 0`() {
        givenPurchases("sku1", "sku2")
        givenCompletedGames(1)
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(false)
    }

    @Test
    fun `it should report ok(false) if purchases is not empty and gameCount 0`() {
        givenPurchases("sku1", "sku2")
        givenCompletedGames(0)
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(false)
    }

    @Test
    fun `it should report ok(false) if purchases is empty and gameCount 0`() {
        givenPurchases()
        givenCompletedGames(0)
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(false)
    }

    @Test
    fun `it should report ok(true) if purchases is empty and gameCount gt 0`() {
        givenPurchases()
        givenCompletedGames(1)
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
        subject = FetchPurchasedItemsUsecase(mockGameRepo, bg, fg)
    }

    private fun givenCompletedGames(numberOfGames: Int){
        givenNumberOfGames = numberOfGames
    }

    private fun givenPurchases(vararg skus: String) {
        givenPurchases = skus.map { it }
    }

    private fun whenFetchingSucceeds() {
        whenever(mockGameRepo.countFinishedGames()).thenReturn(givenNumberOfGames)
        subject.exec(mockDone, mockFail)
    }

    private fun whenFetchingFails() {
        subject.exec(mockDone, mockFail)
    }

    private fun thenOkIsReported(expected: Boolean) {
        verify(mockDone).invoke(FetchPurchasedItemsResponse(expected))
    }

    private fun thenFailureIsReported() {
        verify(mockFail).invoke(any())
    }
}
