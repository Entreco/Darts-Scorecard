package nl.entreco.domain.ad

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.libcore.threading.TestBackground
import nl.entreco.libcore.threading.TestForeground
import nl.entreco.domain.repository.GameRepository
import org.junit.Test
import org.mockito.kotlin.mock

class FetchPurchasedItemsUsecaseTest {

    private var bg = TestBackground()
    private var fg = TestForeground()
    private val mockDone: (FetchPurchasedItemsResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private val mockGameRepo: GameRepository = mock()

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
