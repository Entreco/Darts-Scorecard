package nl.entreco.domain.play.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GameTest : BaseGameTest() {

    @Test
    fun `it should have non null arbiter`() {
        assertNotNull(subject.arbiter)
    }

    @Test
    fun `it should be in NewGame state`() {
        assertEquals("game on", subject.state)
    }

    @Test
    fun `it should set state to Leg when starting`() {
        givenGameStarted()

        assertEquals("1 to throw first", subject.state)
    }

    @Test
    fun `it should subtract darts thrown from score1`() {
        givenGameStarted()
        whenDartsThrown(sixty())

        assertScore(441, 501)
    }

    @Test
    fun `it should set next player after darts thrown1`() {
        givenGameStarted()
        whenDartsThrown(sixty())

        assertEquals("player 2 to throw", subject.state)
    }

    @Test
    fun `it should subtract darts thrown from score2`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty())

        assertScore(441, 441)
    }

    @Test
    fun `it should set next player after darts thrown2`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty())

        assertEquals("player 1 to throw", subject.state)
    }

    @Test
    fun `it should subtract darts thrown from score3`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), sixty())

        assertScore(381, 441)
    }

    @Test
    fun `it should set next player after darts thrown3`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), sixty())

        assertEquals("player 2 to throw", subject.state)
    }

    @Test
    fun `it should subtract darts thrown from score3 multiple`() {
        givenGameStarted()
        whenDartsThrown(sixty(), sixty(), sixty(), sixty(), sixty(), sixty())

        assertScore(321, 321)
    }

    @Test
    fun `it should return correct teams`() {
        givenGameStarted()
        assertNotNull(subject.teams())
    }
}