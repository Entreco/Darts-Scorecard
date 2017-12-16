package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.play.score.GameLoadable
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.RetrieveGameUsecase
import nl.entreco.domain.play.usecase.CreateGameInput
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

    @Mock private lateinit var mockRetrieveGameUsecase: RetrieveGameUsecase
    @Mock private lateinit var mockLoadable: GameLoadable
    @Mock private lateinit var mockScoreListener: ScoreListener
    @Mock private lateinit var mockPlayerListener: PlayerListener
    @Mock private lateinit var mockSpecialListener: SpecialEventListener<*>

    private val createGameInput: CreateGameInput = CreateGameInput(501, 0, 3, 2)
    private val mockArbiter: Arbiter = Arbiter(Score(createGameInput.startScore), TurnHandler(arrayOf(Team(arrayOf(Player("piet"))), Team(arrayOf(Player("puk")))), createGameInput.startIndex))
    private val uid: String = "some uid"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should notify GameLoadable when game was loaded`() {
        givenGameRetrieved()
        whenUiIsReady()
    }

    @Test
    fun `it should not notify GameLoadable when game was loaded`() {
        givenGameRetrieved()
        whenUiIsNotReady()
    }

    @Test
    fun `it should show correct score when initial turn submitted`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20))
        thenScoresAre(arrayOf(Score(441), Score(501)))
    }

    @Test
    fun `it should show correct score when second turn submitted`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20), Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        thenScoresAre(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.TEST_D250))
        thenScoresAre(arrayOf(Score(501, 1, 0), Score(501, 0, 0)))
    }

    @Test
    fun `it should notify scoreListeners on dart thrown`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenScoreListener(mockScoreListener)
        whenDartThrown(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenScoreListenerIsNotifiedOfDartThrown()
    }


    @Test
    fun `it should notify scoreListeners when turns submitted`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenScoreListener(mockScoreListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenScoreListenerIsNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify playerListeners when turns submitted`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenPlayerListener(mockPlayerListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenPlayerListenerIsNotified()
    }

    @Test
    fun `it should notify specialListeners when turns submitted`() {
        givenGameRetrieved()
        whenUiIsReady()
        givenSpecialListener(mockSpecialListener)
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        thenSpecialListenerIsNotified()
    }

    @Test
    fun `it should notify scoreListeners when UiIsReady`() {
        givenGameRetrieved()
        givenScoreListener(mockScoreListener)
        whenUiIsReady()
        thenScoreListenerIsNotifiedOfScoreChange()
    }

    @Test
    fun `it should notify playerListeners when UiIsReady`() {
        givenGameRetrieved()
        givenPlayerListener(mockPlayerListener)
        whenUiIsReady()
        thenPlayerListenerIsNotified()
    }

    @Test
    fun `it should NOT notify specialListeners when UiIsReady`() {
        givenGameRetrieved()
        givenSpecialListener(mockSpecialListener)
        whenUiIsReady()
        thenSpecialListenerIsNotNotified()
    }

    private fun givenScoreListener(vararg listeners: ScoreListener) {
        for (listener in listeners) {
            subject.addScoreListener(listener)
        }
    }

    private fun givenPlayerListener(vararg listeners: PlayerListener) {
        for (listener in listeners) {
            subject.addPlayerListener(listener)
        }
    }

    private fun givenSpecialListener(vararg listeners: SpecialEventListener<*>) {
        for (listener in listeners) {
            subject.addSpecialEventListener(listener)
        }
    }

    private fun givenGameRetrieved() {
        game = Game("uid", mockArbiter)
        subject = Play01ViewModel(mockRetrieveGameUsecase)
        subject.retrieveGame(uid, createGameInput, mockLoadable)
        verify(mockRetrieveGameUsecase).start(eq(uid), any(), any())
    }

    private fun whenUiIsReady() {
        subject.startOk(mockLoadable, createGameInput).invoke(game)
        verify(mockLoadable).startWith(game, createGameInput, subject)
        subject.onLetsPlayDarts()
    }

    private fun whenUiIsNotReady() {
        verify(mockLoadable, never()).startWith(game, createGameInput, subject)
        // Some error callback in the future
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