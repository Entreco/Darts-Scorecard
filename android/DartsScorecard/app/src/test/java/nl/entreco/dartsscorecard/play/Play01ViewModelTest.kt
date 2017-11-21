package nl.entreco.dartsscorecard.play

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.play.usecase.CreateGameUsecase
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
    @Mock private lateinit var mockGameRepository: GameRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `it should show correct score when initial turn submitted`() {
        givenScoreListener()
        givenGameStartedWithInitialScore(Score())
        whenTurnSubmitted(Turn(20, 20, 20))
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
        whenTurnSubmitted(Turn(20, 20, 20), Turn(60, 60, 60))
        verifyScores(arrayOf(Score(441), Score(321)))
    }

    @Test
    fun `it should show correct score when leg is finished`() {
        givenGameStartedWithInitialScore(Score(61, 0, 0, settings = ScoreSettings(61, 2, 2)))
        whenTurnSubmitted(Turn(1, 20, 40))
        verifyScores(arrayOf(Score(61, 1, 0), Score(61, 0, 0)))
    }

    private fun givenGameStartedWithInitialScore(score: Score) {
        val arbiter = Arbiter(score, TurnHandler(arrayOf(Team(Player("1")), Team(Player("2")))))
        whenever(mockGameRepository.new(arbiter)).then { Game(arbiter).apply { start() } }
        subject = Play01ViewModel(CreateGameUsecase(arbiter, mockGameRepository))
    }

    private fun whenTurnSubmitted(vararg turns: Turn) {
        for (turn in turns) {
            subject.handleTurn(turn)
        }
    }

    private fun verifyScores(scores: Array<Score>) {

    }
}