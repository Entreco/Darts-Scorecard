package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.play.score.GameLoadable
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.usecase.RetrieveGameUsecase
import nl.entreco.domain.play.usecase.SetupModel
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

    private val setupModel: SetupModel = SetupModel(501, 0, 3, 2)
    private val mockArbiter: Arbiter = Arbiter(Score(setupModel.startScore), TurnHandler(arrayOf(Team(arrayOf(Player("piet"))), Team(arrayOf(Player("puk")))), setupModel.startIndex))
    private val uid: String = "some uid"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should notify GameLoadable when game was loaded`() {
        givenScoreListener()
        givenGameRetrieved(uid, setupModel, mockLoadable)
        whenUiIsReady()
    }

    @Test
    fun `it should show correct score when initial turn submitted`() {
        givenGameRetrieved(uid, setupModel, mockLoadable)
        whenUiIsReady()
        givenScoreListener()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20))
        verifyScores(arrayOf(Score(441), Score(501)))
    }

    @Test
    fun `it should show correct score when second turn submitted`() {
        givenGameRetrieved(uid, setupModel, mockLoadable)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20), Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        verifyScores(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameRetrieved(uid, setupModel, mockLoadable)
        whenUiIsReady()
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        verifyScores(arrayOf(Score(61, 1, 0), Score(61, 0, 0)))
    }

    @Test
    fun `it should notify listeners on dart thrown`() {
        givenGameRetrieved(uid, setupModel, mockLoadable)
        whenUiIsReady()
        givenScoreListener(mockScoreListener)
        whenDartThrown(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        verify(mockScoreListener).onDartThrown(any(), any())

    }

    private fun givenScoreListener(vararg listeners: ScoreListener) {
        for (listener in listeners) {
            subject.addScoreListener(listener)
        }
    }

    private fun givenGameRetrieved(uid: String, setup: SetupModel, loadable: GameLoadable) {
        game = Game(mockArbiter)
        subject = Play01ViewModel(mockRetrieveGameUsecase)
        subject.retrieveGame(uid, setup, loadable)
        verify(mockRetrieveGameUsecase).start(eq(uid), eq(setupModel), any(), any())
    }

    private fun whenUiIsReady() {
        subject.startOk(mockLoadable, setupModel).invoke(game)
        verify(mockLoadable).startWith(game, setupModel, subject)
        subject.onLetsPlayDarts()
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

    private fun verifyScores(scores: Array<Score>) {
        verify(mockPlayerListener).onNext(any())
    }
}