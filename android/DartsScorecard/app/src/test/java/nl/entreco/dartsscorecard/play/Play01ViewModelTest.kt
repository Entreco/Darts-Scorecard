package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.domain.play.usecase.RetrieveGameUsecase
import nl.entreco.domain.play.usecase.SetupModel
import nl.entreco.domain.settings.ScoreSettings
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Entreco on 14/11/2017.
 */
class Play01ViewModelTest {

    private lateinit var subject: Play01ViewModel

    @Mock private lateinit var mockBg: Background
    @Mock private lateinit var mockFg: Foreground
    private val setupModel: SetupModel = SetupModel(501, 0, 3, 2)


    @Mock private lateinit var mockGameRepository: GameRepository
    @Mock private lateinit var mockScoreViewModel: ScoreViewModel
    @Mock private lateinit var mockInputViewModel: InputViewModel
    @Mock private lateinit var mockGetFinishUsecase: GetFinishUsecase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should show correct score when initial turn submitted`() {
        givenScoreListener()
        givenGameStartedWithInitialScore(Score())
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20))
        verifyScores(arrayOf(Score(441), Score(501)))
    }

    private fun givenScoreListener(vararg listeners: ScoreListener) {
        for (listener in listeners) {
            subject.addScoreListener(listener)
        }
    }

    @Test
    fun `it should show correct score when second turn submitted`() {
        givenGameStartedWithInitialScore(Score())
        whenTurnSubmitted(Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20), Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20))
        verifyScores(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameStartedWithInitialScore(Score(61, 0, 0, settings = ScoreSettings(61, 2, 2)))
        whenTurnSubmitted(Turn(Dart.SINGLE_1, Dart.SINGLE_20, Dart.DOUBLE_20))
        verifyScores(arrayOf(Score(61, 1, 0), Score(61, 0, 0)))
    }

    private fun givenGameStartedWithInitialScore(score: Score) {
        val arbiter = Arbiter(score, TurnHandler(arrayOf(Team(arrayOf(Player("1"))), Team(arrayOf(Player("2"))))))
        whenever(mockGameRepository.create(setupModel)).then { Game(arbiter).apply { start() } }
        subject = Play01ViewModel(RetrieveGameUsecase(mockGameRepository, mockBg, mockFg))
    }

    private fun whenTurnSubmitted(vararg turns: Turn) {
        for (turn in turns) {
            subject.handleTurn(turn, Player(""))
        }
    }

    private fun verifyScores(scores: Array<Score>) {

    }
}