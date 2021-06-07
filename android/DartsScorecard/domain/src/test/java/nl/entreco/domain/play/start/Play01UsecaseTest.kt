package nl.entreco.domain.play.start

import org.mockito.kotlin.*
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.stats.*
import nl.entreco.liblog.Logger
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 17/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01UsecaseTest {

    private val gameId: Long = 42
    private val teams = arrayOf(Team(arrayOf(Player("1"), Player("2"))), Team(arrayOf(Player("3"))), Team(arrayOf(Player("4"))))
    private val teamIds = "1,2|3|4"
    private val startScore = 1001
    private val startIndex = 2
    private val numLegs = 3
    private val numSets = 10
    private val req: Play01Request = Play01Request(gameId, teamIds, startScore, startIndex, numLegs, numSets)

    private val teamOkCaptor = argumentCaptor<(RetrieveTeamsResponse) -> Unit>()
    private val gameOkCaptor = argumentCaptor<(RetrieveGameResponse) -> Unit>()
    private val turnOkCaptor = argumentCaptor<(RetrieveTurnsResponse) -> Unit>()
    private val storeOkCaptor = argumentCaptor<(StoreTurnResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Mock private lateinit var done: (Play01Response) -> Unit
    @Mock private lateinit var fail: (Throwable) -> Unit
    @Mock private lateinit var mockGameUc: RetrieveGameUsecase
    @Mock private lateinit var mockTurnsUc: RetrieveTurnsUsecase
    @Mock private lateinit var mockTeamUc: RetrieveTeamsUsecase
    @Mock private lateinit var mockTurnUc: StoreTurnUsecase
    @Mock private lateinit var mockStatsUc: StoreMetaUsecase
    @Mock private lateinit var mockUndo: UndoTurnUsecase
    @Mock private lateinit var mockMarkUc: MarkGameAsFinishedUsecase
    @Mock private lateinit var mockDelUc: DeleteGameUsecase
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockDone: (Long, Long) -> Unit

    private val mockTurns = emptyList<Pair<Long, Turn>>()
    private lateinit var expectedTurnRequest: StoreTurnRequest
    private lateinit var expectedUndoRequest: UndoTurnRequest
    private lateinit var expectedTurnMeta: TurnMeta
    private lateinit var givenMarkFinishRequest: MarkGameAsFinishedRequest

    private lateinit var subject: Play01Usecase

    @Before
    fun setUp() {
        subject = Play01Usecase(mockGameUc, mockTurnsUc, mockTeamUc, mockTurnUc, mockStatsUc, mockUndo, mockMarkUc, mockDelUc, mockLogger)
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
    fun `it should store stats when storing turns succeeds`() {
        whenStoringTurn(Turn(Dart.DOUBLE_1, Dart.DOUBLE_15))
        whenStoringTurnSucceeds(12L)
        thenStatsAreStored()
    }

    @Test
    fun `it should log error when storing turns fails`() {
        whenStoringTurn(Turn(Dart.DOUBLE_1, Dart.DOUBLE_15))
        whenStoringTurnFails(RuntimeException("Something went wrong"))
        thenErrorIsLogged()
    }

    @Test
    fun markGameAsFinished() {
        whenGameIsFinished()
        thenGameIsMarkedAsFinished()
    }

    @Test
    fun `it should undo last turn`() {
        whenUndoLastTurn()
        thenUndoUsecaseIsExecuted()
    }

    private fun whenStoringTurn(turn: Turn, state: State = State.NORMAL) {
        expectedTurnRequest = StoreTurnRequest(0, gameId, turn, state)
        expectedTurnMeta = TurnMeta(1, 2, 0, 0, Score())
        subject.storeTurnAndMeta(expectedTurnRequest, expectedTurnMeta, mockDone)
    }

    private fun whenStoringTurnSucceeds(id: Long) {
        verify(mockTurnUc).exec(eq(expectedTurnRequest), storeOkCaptor.capture(), any())
        storeOkCaptor.lastValue.invoke(StoreTurnResponse(id, expectedTurnRequest.turn))
    }

    private fun whenStoringTurnFails(err: Throwable) {
        verify(mockTurnUc).exec(eq(expectedTurnRequest), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun thenTurnIsStored() {
        verify(mockTurnUc).exec(eq(expectedTurnRequest), any(), any())
    }

    private fun whenLoadingGameAndStarting() {
        subject.loadGameAndStart(req, done, fail)
    }

    private fun whenTeamsAreRetrieved() {
        verify(mockTeamUc).exec(eq(RetrieveTeamsRequest(teamIds)), teamOkCaptor.capture(), any())
        teamOkCaptor.firstValue.invoke(RetrieveTeamsResponse(teams))
    }

    private fun whenTeamsAreNotRetrieved() {
        verify(mockTeamUc).exec(eq(RetrieveTeamsRequest(teamIds)), any(), failCaptor.capture())
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

    private fun whenGameIsFinished() {
        givenMarkFinishRequest = MarkGameAsFinishedRequest(gameId, "p1")
        subject.markGameAsFinished(givenMarkFinishRequest)
    }

    private fun whenUndoLastTurn() {
        expectedUndoRequest = UndoTurnRequest(1)
        subject.undoLastTurn(expectedUndoRequest, {}, {})
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

    private fun thenErrorIsLogged() {
        verify(mockLogger).w(any())
    }

    private fun thenStatsAreStored() {
        verify(mockStatsUc).exec(any(), any(), any())
    }

    private fun thenUndoUsecaseIsExecuted() {
        verify(mockUndo).exec(eq(expectedUndoRequest), any(), any())
    }
}
