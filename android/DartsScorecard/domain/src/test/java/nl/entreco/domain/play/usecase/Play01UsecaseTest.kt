package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.model.players.TeamIdsString
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 17/12/2017.
 */
class Play01UsecaseTest {

    private val gameId: Long = 42
    private val teams = arrayOf(Team(arrayOf(Player("1"), Player("2"))), Team(arrayOf(Player("3"))), Team(arrayOf(Player("4"))))
    private val teamIds = TeamIdsString("1,2|3|4")
    private val settings = GameSettingsRequest(1001, 2, 3, 10)
    private val req: RetrieveGameRequest = RetrieveGameRequest(gameId, teamIds, settings)

    private val teamOkCaptor = argumentCaptor<(Array<Team>) -> Unit>()
    private val gameOkCaptor = argumentCaptor<(Game) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Mock private lateinit var done: (Game, Array<Team>) -> Unit
    @Mock private lateinit var fail: (Throwable) -> Unit
    @Mock private lateinit var mockGameUc: RetrieveGameUsecase
    @Mock private lateinit var mockTeamUc: RetrieveTeamsUsecase
    @Mock private lateinit var mockGame: Game

    private lateinit var subject: Play01Usecase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = Play01Usecase(mockGameUc, mockTeamUc)
    }

    @Test
    fun loadGameAndStart() {
        whenLoadingGameAndStarting()
        whenTeamsAreRetrieved()
        whenGameIsLoaded()
        thenGameIsStarted()
    }

    @Test
    fun loadGameWithTeamErrors() {
        whenLoadingGameAndStarting()
        whenTeamsAreNotRetrieved()
        thenGameIsNotStarted()
    }


    @Test
    fun loadGameWithGameErrors() {
        whenLoadingGameAndStarting()
        whenTeamsAreRetrieved()
        whenGameIsNotLoaded()
        thenGameIsNotStarted()
    }

    private fun whenLoadingGameAndStarting() {
        subject.loadGameAndStart(req, done, fail)
    }

    private fun whenTeamsAreRetrieved() {
        verify(mockTeamUc).start(eq(teamIds), teamOkCaptor.capture(), any())
        teamOkCaptor.firstValue.invoke(teams)
    }

    private fun whenTeamsAreNotRetrieved() {
        verify(mockTeamUc).start(eq(teamIds), any(), failCaptor.capture())
        failCaptor.firstValue.invoke(Throwable("unable to retrieve teams"))
    }

    private fun whenGameIsLoaded() {
        verify(mockGameUc).start(eq(gameId), gameOkCaptor.capture(), any())
        gameOkCaptor.firstValue.invoke(mockGame)
    }

    private fun whenGameIsNotLoaded() {
        verify(mockGameUc).start(eq(gameId), any(), failCaptor.capture())
        failCaptor.firstValue.invoke(Throwable("cant retrieve game"))
    }

    private fun thenGameIsStarted() {
        verify(mockGame).start(2, teams)
    }

    private fun thenGameIsNotStarted() {
        verify(mockGame, never()).start(2, teams)
    }

}