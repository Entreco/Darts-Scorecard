package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.TestBackground
import nl.entreco.domain.play.TestForeground
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 14/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class RetrieveGameUsecaseTest {

    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var arbiter: Arbiter
    @Mock private lateinit var mockOk: ((Game) -> Unit)
    @Mock private lateinit var mockErr: ((Throwable) -> Unit)

    private lateinit var subject: RetrieveGameUsecase

    private var id: Long = 0
    private lateinit var settings: CreateGameInput
    private lateinit var game: Game
    private lateinit var mockForeground: TestForeground
    private lateinit var mockBackground: TestBackground

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        id = 42
        settings = CreateGameInput(501, 0, 3, 2)
        game = Game(id, arbiter)

        mockForeground = TestForeground()
        mockBackground = TestBackground()
    }

    @Test
    fun `it should create a game and start it`() {
        givenRetrievedGameUsecase()
        whenStartIsCalled(game)
        thenGameIsStarted()
        andOkIsReported()
    }

    @Test
    fun `it should report err when game retrieval failed`() {
        givenRetrievedGameUsecase()
        whenStartIsCalled(null)
        thenErrIsReported()
    }

    private fun givenRetrievedGameUsecase() {
        subject = RetrieveGameUsecase(mockGameRepository, mockBackground, mockForeground)
    }

    private fun whenStartIsCalled(game: Game?) {
        if (game == null) {
            whenever(mockGameRepository.fetchBy(id)).thenThrow(IllegalStateException("game not found"))
        } else {
            whenever(mockGameRepository.fetchBy(id)).thenReturn(game)
        }
        subject.start(id, mockOk, mockErr)
    }

    private fun thenGameIsStarted() {
        verify(mockGameRepository).fetchBy(id)
    }

    private fun andOkIsReported() {
        verify(mockOk).invoke(game)
    }

    private fun thenErrIsReported() {
        verify(mockErr).invoke(any())
    }

}