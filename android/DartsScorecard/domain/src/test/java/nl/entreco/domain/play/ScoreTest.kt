package nl.entreco.domain.play

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
        assertEquals(Score(501, 0, 1), Score(0, 2, 0).rollSet())
    }

    @Test
    fun `it should correctly increment when loosing another leg`() {
        assertEquals(Score(501, 1, 0), Score(10, 1, 0).rollLeg())
    }

    @Test
    fun `it should correctly increment when loosing 1st set`() {
        assertEquals(Score(501, 0, 0), Score(10, 1, 0).rollSet())
    }

    @Test
    fun `it should correctly increment when winning 2nd set`() {
        assertEquals(Score(501, 0, 2), Score(0, 2, 1).rollSet())
    }

    @Test
    fun `it should correctly increment when loosing 2nd set`() {
        assertEquals(Score(501, 0, 1), Score(10, 1, 1).rollSet())
    }

    @Test
    fun `it should correctly increment when winning match`() {
        assertEquals(Score(501, 0, 2), Score(0, 2, 1).rollSet())
    }

    @Test
    fun `it should correctly increment when loosing match`() {
        assertEquals(Score(501, 0, 1), Score(10, 2, 1).rollSet())
    }

    @Test
    fun `it should know when a match is not yet finished`() {
        assertFalse(Score(0, 0, 0).matchFinished())
        assertFalse(Score(0, 1, 0).matchFinished())
        assertFalse(Score(0, 3, 0).matchFinished())
        assertFalse(Score(0, 100, 0).matchFinished())

        assertFalse(Score(0, 0, 1).matchFinished())
        assertFalse(Score(0, 1, 1).matchFinished())
        assertFalse(Score(0, 3, 1).matchFinished())
        assertFalse(Score(0, 100, 1).matchFinished())

        assertFalse(Score(0, 0, 2).matchFinished())
        assertFalse(Score(0, 1, 2).matchFinished())

        assertFalse(Score(1, 2, 2).matchFinished())
        assertFalse(Score(1, 3, 2).matchFinished())
        assertFalse(Score(1, 100, 2).matchFinished())
    }

    @Test
    fun `it should know when a match is finished`() {
        assertTrue(Score(0, 2, 2).matchFinished())
        assertTrue(Score(0, 3, 2).matchFinished())
        assertTrue(Score(0, 100, 2).matchFinished())
    }
}