package nl.entreco.domain.play.usecase

import nl.entreco.domain.Logger
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Turn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 24/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class GetFinishUsecaseTest {

    @Mock private lateinit var mockLogger: Logger
    @InjectMocks lateinit var subject: GetFinishUsecase

    @Test
    fun `it should return empty for scores higher than 170`() {
        assertEquals("", subject.calculateInBack(171, Turn(), 20))
    }

    @Test
    fun `it should return T20 T20 BULL for score of 170`() {
        assertEquals("T20 T20 BULL", subject.calculateInBack(170, Turn(), 20))
    }

    @Test
    fun `it should use correct number of darts when 1 dart left`() {
        val turn = Turn(Dart.ZERO, Dart.ZERO)
        assertEquals(1, turn.dartsLeft())

        for (score in 0..180) {
            when {
                score > 50 -> verifyNotPossible(score, turn)
                score == 48 -> verifyNotPossible(score, turn)
                score == 46 -> verifyNotPossible(score, turn)
                score == 44 -> verifyNotPossible(score, turn)
                score == 42 -> verifyNotPossible(score, turn)
                score == 1 -> verifyNotPossible(score, turn)
                score == 0 -> verifyNotPossible(score, turn)
                score % 2 == 0 -> verifyDartsNeeded(score, turn, 1)
                else -> verifyNotPossible(score, turn)  // not possible in 1 dart
            }
        }
    }

    @Test
    fun `it should use correct number of darts when 2 dart left`() {
        val turn = Turn(Dart.ZERO)
        assertEquals(2, turn.dartsLeft())

        for (score in 0..180) {
            when {
                score >= 101 || score == 99 -> verifyNotPossible(score, turn)
                score >= 42 -> verifyDartsNeeded(score, turn, 2)
                score == 28 || score == 22 -> verifyDartsNeeded(score, turn, 2)
                score == 1 || score == 0 -> verifyNotPossible(score, turn)
                score % 2 == 0 -> verifyDartsNeeded(score, turn, 1)
                else -> verifyDartsNeeded(score, turn, 2)
            }
        }
    }

    @Test
    fun `it should use correct number of darts when 3 dart left`() {
        val turn = Turn()
        assertEquals(3, turn.dartsLeft())

        for (score in 0..180) {
            when {
                score >= 171 -> verifyNotPossible(score, turn)
                score == 169 -> verifyNotPossible(score, turn)
                score == 168 -> verifyNotPossible(score, turn)
                score == 166 -> verifyNotPossible(score, turn)
                score == 165 -> verifyNotPossible(score, turn)
                score == 163 -> verifyNotPossible(score, turn)
                score == 162 -> verifyNotPossible(score, turn)
                score == 159 -> verifyNotPossible(score, turn)
                score >= 101 -> verifyDartsNeeded(score, turn, 3)
                score == 100 -> verifyDartsNeeded(score, turn, 2)
                score == 99 -> verifyDartsNeeded(score, turn, 3)
                score == 32 -> verifyDartsNeeded(score, turn, 1)
                score >= 22 -> verifyDartsNeeded(score, turn, 2)
                score == 18 -> verifyDartsNeeded(score, turn, 2)
                score == 1 -> verifyNotPossible(score, turn)
                score == 0 -> verifyNotPossible(score, turn)
                score % 2 == 0 -> verifyDartsNeeded(score, turn, 1)
                else -> verifyDartsNeeded(score, turn, 2)
            }
        }
    }

    private fun verifyNotPossible(scored: Int, turn: Turn) {
        val finish = subject.calculateInBack(scored, turn, 20)
        assertEquals("scored: $scored finish: $finish", subject.notPossible, finish)
    }

    private fun verifyDartsNeeded(scored: Int, turn: Turn, numDarts: Int) {
        val finish = subject.calculateInBack(scored, turn, 20)
        assertNotEquals("scored: $scored finish: $finish", subject.notPossible, finish)
        assertEquals("scored: $scored finish: $finish", numDarts, finish.split(" ").size)
    }
}