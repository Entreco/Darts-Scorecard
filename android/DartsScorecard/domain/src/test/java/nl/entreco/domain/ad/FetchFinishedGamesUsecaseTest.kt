package nl.entreco.domain.ad

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.GameRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FetchFinishedGamesUsecaseTest {

    private var bg = TestBackground()
    private var fg = TestForeground()
    private val mockDone: (FetchPurchasedItemsResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private val mockGameRepo: GameRepository = mock()

    private lateinit var mSubject: FetchFinishedGamesUsecase

    private var givenNumberOfGames = 0

    @Test
    fun `it should report ok(true) if gameCount gt 1`() {
        givenCompletedGames(1)
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(true)
    }

    @Test
    fun `it should report ok(false) if gameCount 0`() {
        givenCompletedGames(0)
        givenSubject()
        whenFetchingSucceeds()
        thenOkIsReported(false)
    }

    @Test
    fun `it should report failure if finished games cannot be retrieved`() {
        givenSubject()
        whenFetchingFails()
        thenFailureIsReported()
    }

    private fun givenSubject() {
        mSubject = FetchFinishedGamesUsecase(mockGameRepo, bg, fg)
    }

    private fun givenCompletedGames(numberOfGames: Int) {
        givenNumberOfGames = numberOfGames
    }

    private fun whenFetchingSucceeds() {
        whenever(mockGameRepo.countFinishedGames()).thenReturn(givenNumberOfGames)
        mSubject.exec(mockDone, mockFail)
    }

    private fun whenFetchingFails() {
        whenever(mockGameRepo.countFinishedGames()).thenThrow(RuntimeException())
        mSubject.exec(mockDone, mockFail)
    }

    private fun thenOkIsReported(expected: Boolean) {
        verify(mockDone).invoke(FetchPurchasedItemsResponse(expected))
    }

    private fun thenFailureIsReported() {
        verify(mockFail).invoke(any())
    }
}
