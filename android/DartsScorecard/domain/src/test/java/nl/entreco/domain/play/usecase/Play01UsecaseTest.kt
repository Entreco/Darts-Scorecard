package nl.entreco.domain.play.usecase

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
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
    private val settings = CreateGameRequest(1001, 2, 3, 10)
    private val req: RetrieveGameRequest = RetrieveGameRequest(gameId, teamIds, settings)

    private val teamOkCaptor = argumentCaptor<(Array<Team>) -> Unit>()
    private val gameOkCaptor = argumentCaptor<(Game) -> Unit>()
    private val turnOkCaptor = argumentCaptor<(Array<Turn>) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Mock private lateinit var done: (Game, Array<Team>) -> Unit
    @Mock private lateinit var fail: (Throwable) -> Unit
    @Mock private lateinit var mockGameUc: RetrieveGameUsecase
    @Mock private lateinit var mockTurnsUc: RetrieveTurnsUsecase
    @Mock private lateinit var mockTeamUc: RetrieveTeamsUsecase
    @Mock private lateinit var mockStoreUc: StoreTurnUsecase
    @Mock private lateinit var mockGame: Game
    private val mockTurns = emptyArray<Turn>()

    private lateinit var subject: Play01Usecase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = Play01Usecase(mockGameUc, mockTurnsUc, mockTeamUc, mockStoreUc)
    }

    @Test
    fun loadGameAndStart() {
        whenLoadingGameAndStarting()
        whenTeamsAreRetrieved()
        whenGameIsLoaded()
        whenTurnsRetrieved()
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
        verify(mockTeamUc).exec(eq(teamIds), teamOkCaptor.capture(), any())
        teamOkCaptor.firstValue.invoke(teams)
    }

    private fun whenTeamsAreNotRetrieved() {
        verify(mockTeamUc).exec(eq(teamIds), any(), failCaptor.capture())
        failCaptor.firstValue.invoke(Throwable("unable to retrieve teams"))
    }

    private fun whenGameIsLoaded() {
        whenever(mockGame.id).thenReturn(gameId)
        verify(mockGameUc).start(eq(gameId), gameOkCaptor.capture(), any())
        gameOkCaptor.firstValue.invoke(mockGame)
    }

    private fun whenTurnsRetrieved() {
        verify(mockTurnsUc).exec(eq(gameId), turnOkCaptor.capture(), any())
        turnOkCaptor.firstValue.invoke(mockTurns)
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