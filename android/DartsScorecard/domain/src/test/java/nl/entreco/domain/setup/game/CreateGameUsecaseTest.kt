package nl.entreco.domain.setup.game

import nl.entreco.domain.Analytics
import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecaseTest {

    private val mockAnalytics: Analytics = mock()
    private val mockGameRepository: GameRepository = mock()
    private val mockOk: (CreateGameResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()

    private lateinit var subject: CreateGameUsecase

    private var setup = CreateGameRequest(501, 0, 3, 2)
    private var teamString = "a|b"
    private var mockBg = TestBackground()
    private var mockFg = TestForeground()

    @Before
    fun setUp() {
        subject = CreateGameUsecase(mockGameRepository, mockAnalytics, mockBg, mockFg)
    }

    @Test
    fun `it should create a game and start it`() {
        givenCreateGameUsecase()
        whenStartIsCalled()
        thenGameIsStarted()
    }

    @Test
    fun `it should log "game created" when executing usecase`() {
        givenCreateGameUsecase()
        whenStartIsCalled()
        thenSendToAnalytics("Game Created")
    }

    private fun givenCreateGameUsecase() {
        subject = CreateGameUsecase(mockGameRepository, mockAnalytics, mockBg, mockFg)
    }

    private fun whenStartIsCalled() {
        subject.exec(setup, teamString, mockOk, mockFail)
        verify(mockGameRepository).create(eq(teamString), eq(501), eq(0), eq(3), eq(2))
    }

    private fun thenGameIsStarted() {
        verify(mockOk).invoke(any())
    }

    private fun thenSendToAnalytics(expected: String) {
        verify(mockAnalytics).trackAchievement(expected)
    }
}
