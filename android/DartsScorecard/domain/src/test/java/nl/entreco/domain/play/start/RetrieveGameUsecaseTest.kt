package nl.entreco.domain.play.start

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.Game
import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.repository.GameRepository
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
    @Mock private lateinit var mockArbiter: Arbiter
    @Mock private lateinit var mockOk: ((RetrieveGameResponse) -> Unit)
    @Mock private lateinit var mockErr: ((Throwable) -> Unit)

    private lateinit var subject: RetrieveGameUsecase

    private val givenId: Long = 42
    private lateinit var game: Game
    private lateinit var mockForeground: TestForeground
    private lateinit var mockBackground: TestBackground

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        game = Game(givenId, mockArbiter)

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
            whenever(mockGameRepository.fetchBy(any())).thenThrow(IllegalStateException("game not found"))
        } else {
            whenever(mockGameRepository.fetchBy(game.id)).thenReturn(game)
        }
        subject.start(RetrieveGameRequest( givenId) , mockOk, mockErr)
    }

    private fun thenGameIsStarted() {
        verify(mockGameRepository).fetchBy(givenId)
    }

    private fun andOkIsReported() {
        verify(mockOk).invoke(RetrieveGameResponse(game))
    }

    private fun thenErrIsReported() {
        verify(mockErr).invoke(any())
    }

}