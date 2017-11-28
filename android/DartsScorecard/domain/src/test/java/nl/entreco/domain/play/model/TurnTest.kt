package nl.entreco.domain.play.model

import org.junit.Test

import org.junit.Assert.*

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
    fun `last should be NONE, for empty turn`() {
        assertEquals(Dart.NONE, Turn().last())
    }

    @Test
    fun `darts left should be 3, for empty turn`() {
        assertEquals(3, Turn().dartsLeft())
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

    @Test(expected = IllegalStateException::class)
    fun `it is NOT allowed to add NONE to a turn`() {
        Turn() + Dart.NONE
    }

    @Test
    fun `it IS allowed to add ZERO to a turn`() {
        assertEquals(0, (Turn() + Dart.ZERO).total())
    }
}