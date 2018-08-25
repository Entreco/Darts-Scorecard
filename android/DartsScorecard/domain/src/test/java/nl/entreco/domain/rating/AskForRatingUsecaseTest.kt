package nl.entreco.domain.rating

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.RatingPrefRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AskForRatingUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockDone: (AskForRatingResponse) -> Unit
    @Mock private lateinit var mockGameRepo: GameRepository
    @Mock private lateinit var mockRatePrefsRepo: RatingPrefRepository
    private lateinit var subject: AskForRatingUsecase
    private var givenRatedBefore: Boolean = false
    private var givenGamesFinished: Int = 0


    @Before
    fun setUp() {
        subject = AskForRatingUsecase(mockRatePrefsRepo, mockGameRepo, bg, fg)
    }

    @Test
    fun `it should ask for rating when not asked before and more than 3 games finished`() {
        givenRatedBefore(false)
        givenGamesFinished(3)
        thenShouldAskForRating(true)
    }

    @Test
    fun `it should NOT ask for rating when asked before and more than 3 games finished`() {
        givenRatedBefore(true)
        givenGamesFinished(3)
        thenShouldAskForRating(false)
    }

    @Test
    fun `it should NOT ask for rating when not asked before but no games finished`() {
        givenRatedBefore(false)
        givenGamesFinished(0)
        thenShouldAskForRating(false)
    }

    @Test
    fun `it should NOT ask for rating when not asked before and more than 3 games finished`() {
        givenRatedBefore(true)
        givenGamesFinished(0)
        thenShouldAskForRating(false)
    }

    private fun givenRatedBefore(rated: Boolean) {
        givenRatedBefore = rated
    }

    private fun givenGamesFinished(finished: Int) {
        givenGamesFinished = finished
    }

    private fun thenShouldAskForRating(expected: Boolean) {
        whenever(mockRatePrefsRepo.shouldAskToRateApp()).thenReturn(!givenRatedBefore)
        whenever(mockGameRepo.countFinishedGames()).thenReturn(givenGamesFinished)
        subject.go(mockDone, mockFail)
        verify(mockDone).invoke(AskForRatingResponse(expected))
    }
}