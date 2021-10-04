package nl.entreco.domain.play.finish

import nl.entreco.domain.TestBackground
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.play.ScoreEstimator
import nl.entreco.libcore.threading.Background
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Created by Entreco on 24/11/2017.
 */
class GetFinishUsecaseTest {

    private val mockResult: (GetFinishResponse) -> Unit = mock()
    private val mockBg: Background = TestBackground()
    lateinit var subject: GetFinishUsecase

    @Before
    fun setUp() {
        subject = GetFinishUsecase(mockBg)
    }

    @Test
    fun `it should report result`() {
        subject.calculate(GetFinishRequest(Score(170), Turn(), 20), mockResult)
        verify(mockResult).invoke(GetFinishResponse("T20 T20 BULL"))
    }

    @Test
    fun `it should return empty for scores higher than 170`() {
        val turn = Turn()
        assertEquals("", subject.calculateInBack(171, turn.dartsLeft(), turn.total(), 20))
    }

    @Test
    fun `it should return T20 T20 BULL for score of 170`() {
        val turn = Turn()
        assertEquals("T20 T20 BULL", subject.calculateInBack(170, turn.dartsLeft(), turn.total(), 20))
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
                score == 110 -> verifyDartsNeeded(score, turn, 2)
                score == 107 -> verifyDartsNeeded(score, turn, 2)
                score == 104 -> verifyDartsNeeded(score, turn, 2)
                score == 101 -> verifyDartsNeeded(score, turn, 2)
                score >= 101 -> verifyNotPossible(score, turn)
                score == 99 -> verifyNotPossible(score, turn)
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

    @Test
    fun `it should use personal finish (15) with 1 dart used`() {
        assertEquals("D15", subject.calculateInBack(30, 1, 0, 15))
        assertEquals("D15", subject.calculateInBack(30, 2, 0, 15))
        assertEquals("D15", subject.calculateInBack(30, 3, 0, 15))
    }

    @Test
    fun `it should use personal finish (15) with 2 darts used`() {
        assertEquals("", subject.calculateInBack(31, 1, 0, 15))
        assertEquals("1 D15", subject.calculateInBack(31, 2, 0, 15))
        assertEquals("1 D15", subject.calculateInBack(31, 3, 0, 15))
    }

    @Test
    fun `it should use personal finish (15) with 3 darts used`() {
        assertEquals("", subject.calculateInBack(91, 1, 0, 15))
        assertEquals("T17 D20", subject.calculateInBack(91, 2, 0, 15))
        assertEquals("T20 1 D15", subject.calculateInBack(91, 3, 0, 15))
    }


    @Test
    fun `it should use personal finish (1) with 1 dart used`() {
        assertEquals("D15", subject.calculateInBack(30, 1, 0, 1))
        assertEquals("D14 D1", subject.calculateInBack(30, 2, 0, 1))
        assertEquals("D14 D1", subject.calculateInBack(30, 3, 0, 1))
    }

    @Test
    fun `it should use personal finish (1) with 2 darts used`() {
        assertEquals("", subject.calculateInBack(31, 1, 0, 1))
        assertEquals("15 D8", subject.calculateInBack(31, 2, 0, 1))
        assertEquals("D14 1 D1", subject.calculateInBack(31, 3, 0, 1))
    }

    @Test
    fun `it should use personal finish (1) with 3 darts used`() {
        assertEquals("", subject.calculateInBack(91, 1, 0, 1))
        assertEquals("T17 D20", subject.calculateInBack(91, 2, 0, 1))
        assertEquals("T19 D16 D1", subject.calculateInBack(91, 3, 0, 1))
    }

    @Test
    fun `it should use personal finish (20) with 1 dart used`() {
        assertEquals("D15", subject.calculateInBack(30, 1, 0, 20))
        assertEquals("D15", subject.calculateInBack(30, 2, 0, 20))
        assertEquals("14 D8", subject.calculateInBack(30, 3, 0, 20))
    }

    @Test
    fun `it should use personal finish (20) with 2 darts used`() {
        assertEquals("", subject.calculateInBack(31, 1, 0, 20))
        assertEquals("15 D8", subject.calculateInBack(31, 2, 0, 20))
        assertEquals("15 D8", subject.calculateInBack(31, 3, 0, 20))
    }

    @Test
    fun `it should use personal finish (20) with 3 darts used`() {
        assertEquals("", subject.calculateInBack(91, 1, 0, 20))
        assertEquals("T17 D20", subject.calculateInBack(91, 2, 0, 20))
        assertEquals("T17 D20", subject.calculateInBack(91, 3, 0, 20))
    }


    @Test
    fun `it should use personal finish (BULL) with 1 dart used`() {
        assertEquals("BULL", subject.calculateInBack(50, 1, 0, 25))
        assertEquals("BULL", subject.calculateInBack(50, 2, 0, 25))
        assertEquals("BULL", subject.calculateInBack(50, 3, 0, 25))
    }

    @Test
    fun `it should use personal finish (BULL) with 2 darts used`() {
        assertEquals("", subject.calculateInBack(51, 1, 0, 25))
        assertEquals("1 BULL", subject.calculateInBack(51, 2, 0, 25))
        assertEquals("1 BULL", subject.calculateInBack(51, 3, 0, 25))
    }

    @Test
    fun `it should use personal finish (BULL) with 3 darts used`() {
        assertEquals("", subject.calculateInBack(91, 1, 0, 25))
        assertEquals("T17 D20", subject.calculateInBack(91, 2, 0, 25))
        assertEquals("D20 1 BULL", subject.calculateInBack(91, 3, 0, 25))
    }

    private fun verifyNotPossible(scored: Int, turn: Turn) {
        val finish = subject.calculateInBack(scored, turn.dartsLeft(), turn.total(), 20)
        assertEquals("scored: $scored finish: $finish", subject.notPossible, finish)
    }

    private fun verifyDartsNeeded(scored: Int, turn: Turn, numDarts: Int) {
        val finish = subject.calculateInBack(scored, turn.dartsLeft(), turn.total(), 0)
        val thrown = ScoreEstimator().guess(scored, false)
        assertNotEquals("scored: $scored finish: $finish", subject.notPossible, finish)
        assertEquals("scored: $scored finish: $finish", numDarts, finish.split(" ").size)
        assertEquals("scored: $scored thrown: ${thrown.total()}", scored, thrown.total())
        assertEquals("scored: $scored thrown: ${thrown.total()} finish: $finish", scored, finishToDarts(finish))
    }

    private fun finishToDarts(finish: String): Int {
        var turn = Turn()
        for (dart in finish.split(" ")) {
            try {
                val d = Dart.fromString(dart)
                if (d != Dart.NONE) {
                    turn += d
                }
            } catch (oops: IllegalStateException) {
                System.out.println("dart:$dart, $oops")
            }
        }

        return turn.total()
    }
}