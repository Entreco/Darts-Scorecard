package nl.entreco.domain.play

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Turn
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 22/01/2018.
 */
class ScoreEstimatorAtDoubleTest {

    private val subject = ScoreEstimator()

    @Test
    fun `it should return 3 for 40 (0,0,0)`() {
        assertEquals(3, subject.atDouble(Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO), 40))
    }

    @Test
    fun `it should return 3 for 40 (2,0,0)`() {
        assertEquals(3, subject.atDouble(Turn(Dart.SINGLE_2, Dart.ZERO, Dart.ZERO), 40))
    }

    @Test
    fun `it should return 2 for 40 (2,1,0)`() {
        assertEquals(2, subject.atDouble(Turn(Dart.SINGLE_2, Dart.SINGLE_1, Dart.ZERO), 40))
    }

    @Test
    fun `it should return 2 for 40 (1,1,0)`() {
        assertEquals(2, subject.atDouble(Turn(Dart.SINGLE_1, Dart.SINGLE_1, Dart.ZERO), 40))
    }

    @Test
    fun `it should return 1 for 40 (1,0,0)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.SINGLE_1, Dart.ZERO, Dart.ZERO), 40))
    }

    @Test
    fun `it should return 1 for 40 (1,2,0)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.SINGLE_1, Dart.SINGLE_2, Dart.ZERO), 40))
    }

    @Test
    fun `it should return 1 for 40 (1,2,2)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_2), 40))
    }

    @Test
    fun `it should return 0 for 41 (2,2,2)`() {
        assertEquals(0, subject.atDouble(Turn(Dart.SINGLE_2, Dart.SINGLE_2, Dart.SINGLE_2), 41))
    }

    @Test
    fun `it should return 1 for 141 (T20,T19,2)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.SINGLE_2), 141))
    }

    @Test
    fun `it should return 1 for 141 (T19,T20,2)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.TRIPLE_19, Dart.TRIPLE_20, Dart.SINGLE_2), 141))
    }

    @Test
    fun `it should return 1 for 170 (T20,T20,2)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.SINGLE_2), 170))
    }

    @Test
    fun `it should return 1 for 167 (T20,T19,2)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.SINGLE_2), 167))
    }

    @Test
    fun `it should return 1 for 167 (T19,T20,2)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.TRIPLE_19, Dart.TRIPLE_20, Dart.SINGLE_2), 167))
    }

    @Test
    fun `it should return 1 for 40 (D20)`() {
        assertEquals(1, subject.atDouble(Turn(Dart.DOUBLE_20), 40))
    }

    @Test
    fun `it should return 3 for 40 (0,0,D20)`() {
        assertEquals(3, subject.atDouble(Turn(Dart.ZERO, Dart.ZERO, Dart.DOUBLE_20), 40))
    }
}