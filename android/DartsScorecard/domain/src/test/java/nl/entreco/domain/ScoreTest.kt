package nl.entreco.domain

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 11/11/2017.
 */
class ScoreTest{

    @Test
    fun `501 should be default score`() {
        assertEquals(Score(501), Score())
    }

    @Test
    fun `it should correctly increment when winning 1st leg`() {
        assertEquals(Score(501, 1, 0), Score(0, 0, 0).rollLeg())
    }

    @Test
    fun `it should correctly increment when loosing 1st leg`() {
        assertEquals(Score(501, 0, 0), Score(10, 0, 0).rollLeg())
    }

    @Test
    fun `it should correctly increment when winning 2nd leg`() {
        assertEquals(Score(501, 2, 0), Score(0, 1, 0).rollLeg())
    }

    @Test
    fun `it should correctly increment when loosing 2nd leg`() {
        assertEquals(Score(501, 0, 0), Score(10, 0, 0).rollLeg())
    }

    @Test
    fun `it should correctly increment when winning 1st set`() {
        assertEquals(Score(501, 0, 1), Score(0, 2, 0).rollLeg())
    }

    @Test
    fun `it should correctly increment when loosing 1st set`() {
        assertEquals(Score(501, 1, 0), Score(10, 1, 0).rollLeg())
    }

}