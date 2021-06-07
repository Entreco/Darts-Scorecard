package nl.entreco.domain.play.revanche

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.repository.GameRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 19/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class RevancheUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockDone: (RevancheResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockGameRepository: GameRepository
    private lateinit var subject: RevancheUsecase

    private val team1 = Team()
    private val team2 = Team()

    @Test
    fun `it should create new game`() {
        givenSubject()
        whenTakingRevanche()
        thenNewGameIsCreated()
    }

    @Test
    fun `it should start newly created game`() {
        givenSubject()
        whenTakingRevanche()
        thenNewGameIsStarted()
    }

    @Test
    fun `it should report failure 1`() {
        givenSubject()
        whenCreatingGameFails()
        thenErrorIsReported()
    }

    @Test
    fun `it should report failure 2`() {
        givenSubject()
        whenFetchingGameByIdFails()
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = RevancheUsecase(mockGameRepository, bg, fg)
    }

    private fun whenTakingRevanche() {
        whenever(mockGameRepository.fetchBy(any())).thenReturn(mockGame)
        subject.recreateGameAndStart(revanche(), mockDone, mockFail)
    }

    private fun whenCreatingGameFails() {
        whenever(mockGameRepository.create(any(), any(), any(), any(), any())).thenThrow(RuntimeException("Unable to create game"))
        subject.recreateGameAndStart(revanche(), mockDone, mockFail)
    }

    private fun whenFetchingGameByIdFails() {
        whenever(mockGameRepository.fetchBy(any())).thenThrow(RuntimeException("Unable to create game"))
        subject.recreateGameAndStart(revanche(), mockDone, mockFail)
    }

    private fun revanche() =
            RevancheRequest(Play01Request(0, "1|2", 101, 0, 2, 2), arrayOf(team1, team2), 1)

    private fun thenNewGameIsCreated() {
        verify(mockDone).invoke(any())
    }

    private fun thenNewGameIsStarted() {
        verify(mockGame).start(any(), any())
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }

}
