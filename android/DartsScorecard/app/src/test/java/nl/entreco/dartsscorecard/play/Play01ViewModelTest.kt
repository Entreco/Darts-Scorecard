package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.play.score.GameLoadable
import nl.entreco.domain.Logger
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.play.usecase.Play01Usecase
import nl.entreco.domain.repository.RetrieveGameRequest
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
    private lateinit var req: RetrieveGameRequest

    @Mock private lateinit var mockPlayGameUsecase: Play01Usecase
    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockLoadable: GameLoadable
    @Mock private lateinit var mockScoreListener: ScoreListener
    @Mock private lateinit var mockPlayerListener: PlayerListener
    @Mock private lateinit var mockSpecialListener: SpecialEventListener<*>

    private val doneCaptor = argumentCaptor<(Game, Array<Team>) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    private val createGameRequest: CreateGameRequest = CreateGameRequest(501, 0, 3, 2)
    private val givenTeams = arrayOf(Team(arrayOf(Player("p1"))), Team(arrayOf(Player("p2"))))
    private val mockArbiter: Arbiter = Arbiter(Score(createGameRequest.startScore))
    private val gameId: Long = 1002
    private val teamIds = TeamIdsString("1|2")

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
    fun `it should show correct score when initial turn submitted`() {
        givenGameLoadedOk()
        whenAddingPlayerListener(mockPlayerListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20))
        thenScoresAre(arrayOf(Score(441), Score(501)))
    }

    @Test
    fun `it should show correct score when second turn submitted`() {
        givenGameLoadedOk()
        whenAddingPlayerListener(mockPlayerListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20), Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        thenScoresAre(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameLoadedOk()
        whenAddingPlayerListener(mockPlayerListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.TEST_D250))
        thenScoresAre(arrayOf(Score(501, 1, 0), Score(501, 0, 0)))
    }

    @Test
    fun `it should notify scoreListeners on dart thrown`() {
        givenGameLoadedOk()
        whenAddingScoreListener(mockScoreListener)
        whenDartThrown(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenScoreListenerIsNotifiedOfDartThrown()
    }


    @Test
    fun `it should notify scoreListeners when turns submitted`() {
        givenGameLoadedOk()
        whenAddingScoreListener(mockScoreListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenScoreListenerIsNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify playerListeners when turns submitted`() {
        givenGameLoadedOk()
        whenAddingPlayerListener(mockPlayerListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenPlayerListenerIsNotified()
    }

    @Test
    fun `it should notify specialListeners when turns submitted`() {
        givenGameLoadedOk()
        whenAddingSpecialListener(mockSpecialListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenSpecialListenerIsNotified()
    }

    @Test
    fun `it should notify scoreListeners when UiIsReady`() {
        givenGameLoadedOk()
        whenAddingScoreListener(mockScoreListener)
        whenLetsPlayDarts()
        thenScoreListenerIsNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify playerListeners when UiIsReady`() {
        givenGameLoadedOk()
        whenAddingPlayerListener(mockPlayerListener)
        whenLetsPlayDarts()
        thenPlayerListenerIsNotified()
    }

    @Test
    fun `it should NOT notify specialListeners when UiIsReady`() {
        givenGameLoadedOk()
        whenAddingSpecialListener(mockSpecialListener)
        whenLetsPlayDarts()
        thenSpecialListenerIsNotNotified()
    }

    private fun givenGameAndRequest() {
        game = Game(101, mockArbiter).start(0, givenTeams)
        req = RetrieveGameRequest(gameId, teamIds, createGameRequest)
        subject = Play01ViewModel(mockPlayGameUsecase, mockLogger)
        subject.load(req, mockLoadable)
    }

    private fun whenLoadingOk() {
        verify(mockPlayGameUsecase).loadGameAndStart(eq(req), doneCaptor.capture(), any())
        doneCaptor.firstValue.invoke(game, givenTeams)
    }

    private fun thenUiIsReady() {
        verify(mockLoadable).startWith(givenTeams, createGameRequest, subject)
    }

    private fun thenUiIsNotReady() {
        verify(mockLoadable, never()).startWith(givenTeams, createGameRequest, subject)
        verify(mockLogger).e(any())
    }

    private fun givenGameLoadedOk() {
        givenGameAndRequest()
        whenLoadingOk()
        thenUiIsReady()
    }

    private fun whenLetsPlayDarts() {
        subject.onLetsPlayDarts()
    }

    private fun whenAddingScoreListener(vararg listeners: ScoreListener) {
        for (listener in listeners) {
            subject.addScoreListener(listener)
        }
    }

    private fun whenAddingPlayerListener(vararg listeners: PlayerListener) {
        for (listener in listeners) {
            subject.addPlayerListener(listener)
        }
    }

    private fun whenAddingSpecialListener(vararg listeners: SpecialEventListener<*>) {
        for (listener in listeners) {
            subject.addSpecialEventListener(listener)
        }
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

    private fun thenScoresAre(expected: Array<Score>) {
        assertArrayEquals(expected, game.scores)
    }

    private fun thenPlayerListenerIsNotified() {
        verify(mockPlayerListener).onNext(any())
    }

    private fun thenScoreListenerIsNotifiedOfScoreChange() {
        verify(mockScoreListener).onScoreChange(any(), any())
    }

    private fun thenScoreListenerIsNotifiedOfDartThrown() {
        verify(mockScoreListener).onDartThrown(any(), any())
    }

    private fun thenSpecialListenerIsNotNotified() {
        verify(mockSpecialListener, never()).onSpecialEvent(any(), any(), any(), any())
    }

    private fun thenSpecialListenerIsNotified() {
        verify(mockSpecialListener).onSpecialEvent(any(), any(), any(), any())
    }
}