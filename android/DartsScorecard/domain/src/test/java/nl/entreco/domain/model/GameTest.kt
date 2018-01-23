package nl.entreco.domain.model

import org.junit.Assert.*
import org.junit.Test

class GameTest : BaseGameTest() {

    @Test
    fun `it should have non null arbiter`() {
        assertNotNull(subject.arbiter)
    }

    @Test
    fun `it should subtract darts thrown from score1`() {
        givenGameStarted()
        whenDartsThrown(sixty())

        assertScore(441, 501)
    }

    @Test
    fun `it should subtract darts thrown from score2`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty())

        assertScore(441, 441)
    }

    @Test
    fun `it should subtract darts thrown from score3`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), sixty())

        assertScore(381, 441)
    }

    @Test
    fun `it should subtract darts thrown from score3 multiple`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), sixty(), sixty(), sixty(), sixty())

        assertScore(321, 321)
    }

    @Test
    fun `it should keep track of previous score for stats (start)`() {
        givenGameStarted()
        assertEquals(Score(), subject.previousScore())
    }

    @Test
    fun `it should keep track of previous score for stats (turn 1)`() {
        givenGameStarted()
        whenDartsThrown(sixty())
        assertEquals(Score(501), subject.previousScore())
    }

    @Test
    fun `it should keep track of previous score for stats (turn 2)`() {
        givenGameStarted()
        whenDartsThrown(sixty(), oneEighty())
        assertEquals(Score(501), subject.previousScore())
    }

    @Test
    fun `it should keep track of previous score for stats (turn 3)`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), sixty())
        assertEquals(Score(501-60), subject.previousScore())
    }

    @Test
    fun `it should not report breaks when game just started`() {
        givenGameStarted()
        assertFalse(subject.wasBreakMade(testProvider.player1()))
        assertFalse(subject.wasBreakMade(testProvider.player2()))
    }

    @Test
    fun `it should know when no break was made - normal case`() {
        givenGameStarted()
        whenDartsThrown(sixty(), oneEighty())
        assertFalse(subject.wasBreakMade(testProvider.player1()))
        assertFalse(subject.wasBreakMade(testProvider.player2()))
    }

    @Test
    fun `it should know when no break was made - opponent break`() {
        givenGameStarted()
        whenDartsThrown(sixty(), oneEighty(), sixty(), oneEighty(), sixty(), oneFourOne())
        assertFalse(subject.wasBreakMade(testProvider.player1()))
    }

    @Test
    fun `it should know when break was made`() {
        givenGameStarted()
        whenDartsThrown(sixty(), oneEighty(), sixty(), oneEighty(), sixty(), oneFourOne())
        assertTrue(subject.wasBreakMade(testProvider.player2()))
    }

    @Test
    fun `it should know if match,set or let was started`() {
        givenGameStarted()
        assertTrue(subject.isNewMatchLegOrSet())
        whenDartsThrown(oneEighty(), sixty(), oneEighty(), sixty())
        assertFalse(subject.isNewMatchLegOrSet())
        whenDartsThrown(oneFourOne())
        assertTrue(subject.isNewMatchLegOrSet())
    }

    @Test
    fun `it should increment turnCount after a turn`() {
        givenGameStarted()
        assertEquals(0, subject.getTurnCount())
        whenDartsThrown(sixty())
        assertEquals(1, subject.getTurnCount())
    }

    @Test
    fun `it should increment turnCount after multiple turns`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), oneEighty(), oneFourOne())
        assertEquals(4, subject.getTurnCount())
    }
}