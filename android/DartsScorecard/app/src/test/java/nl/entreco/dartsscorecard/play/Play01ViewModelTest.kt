package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.start.MarkGameAsFinishedRequest
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.setup.game.CreateGameRequest
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 14/11/2017.
 */
class Play01ViewModelTest {

    private lateinit var subject: Play01ViewModel
    private lateinit var game: Game
    private lateinit var req: Play01Request
    private lateinit var givenTeamScoreListeners: List<TeamScoreListener>

    @Mock private lateinit var mockNext: Next
    @Mock private lateinit var mockGame: Game
    @Mock private lateinit var mockScore: Score
    @Mock private lateinit var mockRequest: Play01Request
    @Mock private lateinit var mockPlayGameUsecase: Play01Usecase
    @Mock private lateinit var mock01Listeners: Play01Listeners
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockCreatedNotifier: GameLoadedNotifier<CreateGameRequest>
    @Mock private lateinit var mockTeamScoreListener: TeamScoreListener

    private val doneCaptor = argumentCaptor<(Play01Response) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    private val createGameRequest: CreateGameRequest = CreateGameRequest(501, 0, 3, 2)
    private val givenTeams = arrayOf(Team(arrayOf(Player("p1"))), Team(arrayOf(Player("p2"))))
    private val givenScores = arrayOf(Score(), Score())
    private val givenArbiter: Arbiter = Arbiter(Score(createGameRequest.startScore))
    private val gameId: Long = 1002
    private val teamIds = "1|2"


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should notify ui when game was loaded`() {
        givenGameAndRequest()
        whenLoadingOk()
        thenUiIsReady()
    }

    @Test
    fun `it should not notify ui when game was NOT loaded`() {
        givenGameAndRequest()
        whenLoadingFails(Throwable("something goes wrong"))
        thenUiIsNotReady()
    }

    @Test
    fun `it should register listeners`() {
        givenGameLoadedOk()
        whenRegisteringListeners()
        thenPlay01ListenersAreRegistered()
    }

    @Test
    fun `it should show correct score when initial turn submitted`() {
        givenGameLoadedOk()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20))
        thenScoresAre(arrayOf(Score(441), Score(501)))
    }

    @Test
    fun `it should show correct score when second turn submitted`() {
        givenGameLoadedOk()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20), Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        thenScoresAre(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameLoadedOk()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.TEST_D250))
        thenScoresAre(arrayOf(Score(501, 1, 0), Score(501, 0, 0)))
    }

    @Test
    fun `it should notify scoreListeners on dart thrown`() {
        givenGameLoadedOk()
        whenDartThrown(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenListenersAreNotifiedOfDartThrown()
    }

    @Test
    fun `it should notify scoreListeners when turns submitted`() {
        givenGameLoadedOk()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        then01ListenersAreNotified()
    }


    @Test
    fun `it should notify playerListeners when turns submitted`() {
        givenGameLoadedOk()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        then01ListenersAreNotified()
    }

    @Test
    fun `it should notify specialListeners when turns submitted`() {
        givenGameLoadedOk()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        then01ListenersAreNotified()
    }

    @Test
    fun `it should add TeamScoreListeners when UiIsReady`() {
        givenGameLoadedOk()
        whenLetsPlayDarts()
        thenListenersAreNotifiedOfLetsPlayDarts()
    }

    @Test
    fun `it should notify playerListeners when UiIsReady`() {
        givenGameLoadedOk()
        whenLetsPlayDarts()
        thenListenersAreNotifiedOfLetsPlayDarts()
    }

    @Test
    fun `it should mark game finished when state == MATCH`() {
        givenFullyLoadedMockGame()
        whenNextStateIs(State.MATCH)
        thenGameIsMarkedAsFinished()
    }

    @Test
    fun `it should NOT mark game finished when state != MATCH`() {
        givenGameLoadedOk()
        whenNextStateIs(State.START)
        thenGameIsNotMarkedAsFinished()
    }

    private fun givenGameAndRequest(vararg loaders: GameLoadedNotifier<CreateGameRequest>) {
        game = Game(101, givenArbiter).start(0, givenTeams)
        req = Play01Request(gameId, teamIds, createGameRequest.startScore, createGameRequest.startIndex, createGameRequest.numLegs, createGameRequest.numSets)
        givenTeamScoreListeners = listOf(mockTeamScoreListener, mockTeamScoreListener)
        subject = Play01ViewModel(mockPlayGameUsecase, mock01Listeners, mockLogger)
        subject.load(req, mockCreatedNotifier, *loaders)
    }

    private fun givenFullyLoadedMockGame() {
        subject = Play01ViewModel(mockPlayGameUsecase, mock01Listeners, mockLogger)
        subject.load(mockRequest, mockCreatedNotifier)
        verify(mockPlayGameUsecase).loadGameAndStart(any(), doneCaptor.capture(), any())
        doneCaptor.firstValue.invoke(Play01Response(mockGame, givenTeams))
    }

    private fun whenLoadingOk() {
        verify(mockPlayGameUsecase).loadGameAndStart(eq(req), doneCaptor.capture(), any())
        doneCaptor.firstValue.invoke(Play01Response(game, givenTeams))
    }

    private fun thenUiIsReady() {
        verify(mockCreatedNotifier).onLoaded(givenTeams, givenScores, createGameRequest, subject)
    }

    private fun thenUiIsNotReady() {
        verify(mockCreatedNotifier, never()).onLoaded(givenTeams, givenScores, createGameRequest, subject)
        verify(mockLogger).e(any())
    }

    private fun givenGameLoadedOk() {
        givenGameAndRequest()
        whenLoadingOk()
        thenUiIsReady()
    }

    private fun whenLetsPlayDarts() {
        subject.onLetsPlayDarts(givenTeamScoreListeners)
    }

    private fun whenLoadingFails(err: Throwable) {
        verify(mockPlayGameUsecase).loadGameAndStart(eq(req), any(), failCaptor.capture())
        failCaptor.firstValue.invoke(err)
    }

    private fun whenTurnSubmitted(vararg turns: Turn) {
        for (turn in turns) {
            subject.onTurnSubmitted(turn, Player(""))
        }
    }

    private fun whenDartThrown(vararg turns: Turn) {
        for (turn in turns) {
            subject.onDartThrown(turn, Player(""))
        }
    }

    private fun whenNextStateIs(state: State) {
        val turn = Turn()
        whenever(mockNext.state).thenReturn(state)
        whenever(mockGame.previousScore()).thenReturn(mockScore)
        whenever(mockGame.next).thenReturn(mockNext)
        whenever(mockGame.id).thenReturn(gameId)
        subject.onTurnSubmitted(turn, Player("you won"))
    }

    private fun whenRegisteringListeners() {
        val mockScoreListener = mock<ScoreListener>()
        val mockSpecialEventListener = mock<SpecialEventListener<*>>()
        val mockPlayerListener = mock<PlayerListener>()
        subject.registerListeners(mockScoreListener, mockSpecialEventListener, mockPlayerListener)
    }

    private fun thenScoresAre(expected: Array<Score>) {
        assertArrayEquals(expected, game.scores)
    }

    private fun then01ListenersAreNotified() {
        verify(mock01Listeners).onTurnSubmitted(any(), any(), any(), anyArray())
    }

    private fun thenListenersAreNotifiedOfDartThrown() {
        verify(mock01Listeners).onDartThrown(any(), any())
    }

    private fun thenListenersAreNotifiedOfLetsPlayDarts() {
        verify(mock01Listeners).onLetsPlayDarts(any(), eq(givenTeamScoreListeners))
    }

    private fun thenGameIsMarkedAsFinished() {
        verify(mockPlayGameUsecase).markGameAsFinished(MarkGameAsFinishedRequest(gameId))
    }

    private fun thenGameIsNotMarkedAsFinished() {
        verify(mockPlayGameUsecase, never()).markGameAsFinished(any())
    }

    private fun thenPlay01ListenersAreRegistered() {
        verify(mock01Listeners).registerListeners(any(), any(), any())
    }
}