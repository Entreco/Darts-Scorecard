package nl.entreco.domain.play.start

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.stats.StoreTurnRequest
import nl.entreco.domain.play.stats.StoreTurnUsecase
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
    private val teamIds = "1,2|3|4"
    private val startScore = 1001
    private val startIndex = 2
    private val numLegs = 3
    private val numSets = 10
    private val req: Play01Request = Play01Request(gameId, teamIds , startScore, startIndex, numLegs, numSets)

    private val teamOkCaptor = argumentCaptor<(RetrieveTeamsResponse) -> Unit>()
    private val gameOkCaptor = argumentCaptor<(RetrieveGameResponse) -> Unit>()
    private val turnOkCaptor = argumentCaptor<(RetrieveTurnsResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Mock private lateinit var done: (Play01Response) -> Unit
    @Mock private lateinit var fail: (Throwable) -> Unit
    @Mock private lateinit var mockGameUc: RetrieveGameUsecase
    @Mock private lateinit var mockTurnsUc: RetrieveTurnsUsecase
    @Mock private lateinit var mockTeamUc: RetrieveTeamsUsecase
    @Mock private lateinit var mockStoreUc: StoreTurnUsecase
    @Mock private lateinit var mockMarkUc: MarkGameAsFinishedUsecase
    @Mock private lateinit var mockGame: Game
    private val mockTurns = emptyArray<Turn>()
    private lateinit var expectedTurnRequest: StoreTurnRequest
    private lateinit var givenMarkFinishRequest: MarkGameAsFinishedRequest

    private lateinit var subject: Play01Usecase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        subject = Play01Usecase(mockGameUc, mockTurnsUc, mockTeamUc, mockStoreUc, mockMarkUc)
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

    @Test
    fun storeTurn() {
        whenStoringTurn(Turn(Dart.DOUBLE_1, Dart.DOUBLE_15))
        thenTurnIsStored()
    }

    @Test
    fun markGameAsFinished() {
        whenGameIsFinished()
        thenGameIsMarkedAsFinished()
    }

    private fun whenStoringTurn(turn: Turn) {
        expectedTurnRequest = StoreTurnRequest(gameId, turn)
        subject.storeTurn(expectedTurnRequest)
    }

    private fun thenTurnIsStored() {
        verify(mockStoreUc).exec(expectedTurnRequest)
    }

    private fun whenLoadingGameAndStarting() {
        subject.loadGameAndStart(req, done, fail)
    }

    private fun whenTeamsAreRetrieved() {
        verify(mockTeamUc).exec(eq(RetrieveTeamsRequest(teamIds.toString())), teamOkCaptor.capture(), any())
        teamOkCaptor.firstValue.invoke(RetrieveTeamsResponse(teams))
    }

    private fun whenTeamsAreNotRetrieved() {
        verify(mockTeamUc).exec(eq(RetrieveTeamsRequest(teamIds.toString())), any(), failCaptor.capture())
        failCaptor.firstValue.invoke(Throwable("unable to retrieve teams"))
    }

    private fun whenGameIsLoaded() {
        whenever(mockGame.id).thenReturn(gameId)
        verify(mockGameUc).start(eq(RetrieveGameRequest(gameId)), gameOkCaptor.capture(), any())
        gameOkCaptor.firstValue.invoke(RetrieveGameResponse(mockGame))
    }

    private fun whenTurnsRetrieved() {
        verify(mockTurnsUc).exec(eq(RetrieveTurnsRequest(gameId)), turnOkCaptor.capture(), any())
        turnOkCaptor.firstValue.invoke(RetrieveTurnsResponse(mockTurns))
    }

    private fun whenGameIsNotLoaded() {
        verify(mockGameUc).start(eq(RetrieveGameRequest(gameId)), any(), failCaptor.capture())
        failCaptor.firstValue.invoke(Throwable("cant retrieve game"))
    }

    private fun whenGameIsFinished(){
        givenMarkFinishRequest = MarkGameAsFinishedRequest(gameId)
        subject.markGameAsFinished(givenMarkFinishRequest)
    }

    private fun thenGameIsStarted() {
        verify(mockGame).start(2, teams)
    }

    private fun thenGameIsNotStarted() {
        verify(mockGame, never()).start(2, teams)
    }

    private fun thenGameIsMarkedAsFinished() {
        verify(mockMarkUc).exec(givenMarkFinishRequest)
    }

}