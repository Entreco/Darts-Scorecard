package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.play.TestBackground
import nl.entreco.domain.play.TestForeground
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.repository.GameRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 12/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class CreateGameUsecaseTest {

    @Mock private lateinit var mockCallback: CreateGameUsecase.Callback
    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var mockGame: Game

    private lateinit var subject: CreateGameUsecase

    private var setup = CreateGameInput(501, 0, 3, 2)
    private var teamString = TeamsString("a|b")
    private var mockBg = TestBackground()
    private var mockFg = TestForeground()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should create a game and start it`() {
        givenCreateGameUsecase()
        whenStartIsCalled()
        thenGameIsStarted()
    }

    @Test
    fun `it should return existing game when fetching latest`() {
        givenCreateGameUsecase()
        givenExistingGame()
        whenFetchLatestIsCalled()
        thenGameIsRetrieved()
    }

    @Test
    fun `it should fetch latest`() {
        givenCreateGameUsecase()
        givenNoExistingGame()
        whenFetchLatestIsCalled()
        thenErrorIsReportedBack()
    }

    private fun givenCreateGameUsecase() {
        subject = CreateGameUsecase(mockGameRepository, mockBg, mockFg)
    }

    private fun givenExistingGame() {
        whenever(mockGameRepository.fetchLatest()).thenReturn(mockGame)
    }

    private fun givenNoExistingGame() {
        whenever(mockGameRepository.fetchLatest()).thenThrow(IllegalStateException("ohno"))
    }

    private fun whenStartIsCalled() {
        subject.start(setup, teamString, mockCallback)
        verify(mockGameRepository).create(any(), eq(teamString.asString()), eq(501), eq(0), eq(3), eq(2))
    }

    private fun whenFetchLatestIsCalled() {
        subject.fetchLatest(setup, mockCallback)
        verify(mockGameRepository).fetchLatest()
    }

    private fun thenGameIsStarted() {
        verify(mockCallback).onGameCreated(any(), eq(setup))
    }

    private fun thenGameIsRetrieved() {
        verify(mockCallback).onGameRetrieved(mockGame, setup)
    }

    private fun thenErrorIsReportedBack() {
        verify(mockCallback).onGameRetrieveFailed(isA())
    }
}