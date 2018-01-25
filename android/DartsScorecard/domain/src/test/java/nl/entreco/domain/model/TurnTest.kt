package nl.entreco.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 27/11/2017.
 */
class TurnTest {

    @Test(expected = IllegalStateException::class)
    fun `operator + test`() {
        var turn = Turn()
        assertEquals(0, turn.total())

        turn += Dart.SINGLE_20
        assertEquals(20, turn.total())

        turn += Dart.TRIPLE_12
        assertEquals(56, turn.total())

        turn += Dart.SINGLE_BULL
        assertEquals(81, turn.total())

        turn += Dart.SINGLE_20  // this should throw illegalstate, because we only have 3 darts / turn
    }

    @Test
    fun `first should be NONE, for empty turn`() {
        assertEquals(Dart.NONE, Turn().first())
    }

    @Test
    fun `second should be NONE, for empty turn`() {
        assertEquals(Dart.NONE, Turn().second())
    }

    @Test
    fun `third should be NONE, for empty turn`() {
        assertEquals(Dart.NONE, Turn().third())
    }

    @Test
    fun `last should be NONE, for empty turn`() {
        assertEquals(Dart.NONE, Turn().last())
    }


    @Test
    fun `1st should be same as last`() {
        val turn = Turn(Dart.TRIPLE_7)
        assertEquals(Dart.TRIPLE_7, turn.first())
        assertEquals(Dart.TRIPLE_7, turn.last())
    }

    @Test
    fun `2nd should be same as last`() {
        val turn = Turn(Dart.SINGLE_1, Dart.BULL)
        assertEquals(Dart.BULL, turn.second())
        assertEquals(Dart.BULL, turn.last())
    }

    @Test
    fun `3rd should be same as last`() {
        val turn = Turn(Dart.SINGLE_1, Dart.BULL, Dart.ZERO)
        assertEquals(Dart.ZERO, turn.third())
        assertEquals(Dart.ZERO, turn.last())
    }

    @Test
    fun `darts left should be 3, for empty turn`() {
        assertEquals(3, Turn().dartsLeft())
    }

    @Test
    fun `darts used should be 0, for empty turn`() {
        assertEquals(0, Turn().dartsUsed())
    }

    @Test
    fun `total should be 0, for empty turn`() {
        assertEquals(0, Turn().total())
    }

    @Test
    fun `asFinish should be empty, for empty turn`() {
        assertEquals("", Turn().asFinish())
    }

    @Test
    fun `it should update darts left`() {
        var turn = Turn()
        assertEquals(3, turn.dartsLeft())

        turn += Dart.SINGLE_20
        assertEquals(2, turn.dartsLeft())

        turn += Dart.TRIPLE_2
        assertEquals(1, turn.dartsLeft())

        turn += Dart.BULL
        assertEquals(0, turn.dartsLeft())
    }

    @Test
    fun `it should update darts used`() {
        var turn = Turn()
        assertEquals(0, turn.dartsUsed())

        turn += Dart.SINGLE_20
        assertEquals(1, turn.dartsUsed())

        turn += Dart.TRIPLE_2
        assertEquals(2, turn.dartsUsed())

        turn += Dart.BULL
        assertEquals(3, turn.dartsUsed())
    }

    @Test(expected = IllegalStateException::class)
    fun `it is NOT allowed to add NONE to a turn`() {
        Turn() + Dart.NONE
    }

    @Test
    fun `it IS allowed to add ZERO to a turn`() {
        assertEquals(0, (Turn() + Dart.ZERO).total())
    }

    @Test
    fun `it should return first dart correctly`() {
        assertEquals(Dart.NONE, Turn().first())
        assertEquals(Dart.SINGLE_BULL, Turn(Dart.SINGLE_BULL).first())
        assertEquals(Dart.SINGLE_BULL, Turn(Dart.SINGLE_BULL, Dart.TRIPLE_2).first())
        assertEquals(Dart.SINGLE_BULL, Turn(Dart.SINGLE_BULL, Dart.SINGLE_2, Dart.TRIPLE_20).first())
    }

    @Test
    fun `it should print nicely (= subjective) formatted string`() {
        assertEquals("10 T20 D15 (100)", Turn(Dart.SINGLE_10, Dart.TRIPLE_20, Dart.DOUBLE_15).toString())
    }

    @Test
    fun `it should print nicely (= subjective) as Finish string`() {
        assertEquals("", Turn().asFinish())
        assertEquals("D20", Turn(Dart.DOUBLE_20).asFinish())
        assertEquals("10 D15", Turn(Dart.SINGLE_10, Dart.DOUBLE_15).asFinish())
        assertEquals("10 T20 D15", Turn(Dart.SINGLE_10, Dart.TRIPLE_20, Dart.DOUBLE_15).asFinish())
        assertEquals("BULL", Turn(Dart.BULL).asFinish())
        assertEquals("S.BULL BULL", Turn(Dart.SINGLE_BULL, Dart.BULL).asFinish())
    }
}