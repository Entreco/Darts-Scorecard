package nl.entreco.domain.launch.usecase

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.executors.TestBackground
import nl.entreco.domain.executors.TestForeground
import nl.entreco.domain.model.Game
import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.GameRepository
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

    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockOk: (RetrieveGameRequest) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit


    private lateinit var subject: CreateGameUsecase

    private var setup = CreateGameRequest(501, 0, 3, 2)
    private var teamString = TeamIdsString("a|b")
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
        subject.start(setup, teamString, mockOk, mockFail)
        verify(mockGameRepository).create(eq(teamString.toString()), eq(501), eq(0), eq(3), eq(2))
    }

    private fun whenFetchLatestIsCalled() {
        subject.fetchLatest(setup, teamString, mockOk, mockFail)
        verify(mockGameRepository).fetchLatest()
    }

    private fun thenGameIsStarted() {
        verify(mockOk).invoke(any())
    }

    private fun thenGameIsRetrieved() {
        verify(mockOk).invoke(any())
    }

    private fun thenErrorIsReportedBack() {
        verify(mockFail).invoke(any())
    }
}