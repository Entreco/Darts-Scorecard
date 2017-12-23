package nl.entreco.domain.model

import org.junit.Assert.assertNotNull
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
}