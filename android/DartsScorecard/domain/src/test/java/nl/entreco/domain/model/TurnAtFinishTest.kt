package nl.entreco.domain.model

import nl.entreco.domain.play.ScoreEstimator
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 28/01/2018.
 */
class TurnAtFinishTest{

    private val estimator = ScoreEstimator()

    @Test
    fun `it should correct for Darts at Finish (2)`() {
        assertEquals(Turn(Dart.DOUBLE_1), estimator.guess(2, false).setDartsUsedForFinish(1))
        assertEquals(Turn(Dart.ZERO, Dart.DOUBLE_1), estimator.guess(2, false).setDartsUsedForFinish(2))
        assertEquals(Turn(Dart.ZERO, Dart.ZERO, Dart.DOUBLE_1), estimator.guess(2, false).setDartsUsedForFinish(3))
    }

    @Test
    fun `it should correct for Darts at Finish (3)`() {
        assertEquals(Turn(Dart.SINGLE_1, Dart.DOUBLE_1), estimator.guess(3, false).setDartsUsedForFinish(2))
        assertEquals(Turn(Dart.SINGLE_1, Dart.ZERO, Dart.DOUBLE_1), estimator.guess(3, false).setDartsUsedForFinish(3))
    }

    @Test
    fun `it should correct for Darts at Finish (21)`() {
        assertEquals(Turn(Dart.SINGLE_5, Dart.DOUBLE_8), estimator.guess(21, false).setDartsUsedForFinish(2))
        assertEquals(Turn(Dart.SINGLE_5, Dart.ZERO, Dart.DOUBLE_8), estimator.guess(21, false).setDartsUsedForFinish(3))
    }

    @Test
    fun `it should correct for Darts at Finish (20)`() {
        assertEquals(Turn(Dart.DOUBLE_10), estimator.guess(20, false).setDartsUsedForFinish(1))
        assertEquals(Turn(Dart.ZERO, Dart.DOUBLE_10), estimator.guess(20, false).setDartsUsedForFinish(2))
        assertEquals(Turn(Dart.ZERO, Dart.ZERO, Dart.DOUBLE_10), estimator.guess(20, false).setDartsUsedForFinish(3))
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw if impossible combination (21 in 1)`() {
        estimator.guess(21, false).setDartsUsedForFinish(1)
    }


    @Test(expected = IllegalStateException::class)
    fun `it should throw if impossible combination (3 in 1)`() {
        estimator.guess(3, false).setDartsUsedForFinish(1)
    }
}