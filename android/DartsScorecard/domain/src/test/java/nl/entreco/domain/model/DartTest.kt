package nl.entreco.domain.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 24/11/2017.
 */
class DartTest {

    @Test
    fun `it should correctly determine doubles`() {
        assertTrue(Dart.DOUBLE_1.isDouble())
        assertTrue(Dart.DOUBLE_2.isDouble())
        assertTrue(Dart.DOUBLE_3.isDouble())
        assertTrue(Dart.DOUBLE_4.isDouble())
        assertTrue(Dart.DOUBLE_5.isDouble())
        assertTrue(Dart.DOUBLE_6.isDouble())
        assertTrue(Dart.DOUBLE_7.isDouble())
        assertTrue(Dart.DOUBLE_8.isDouble())
        assertTrue(Dart.DOUBLE_9.isDouble())
        assertTrue(Dart.DOUBLE_10.isDouble())
        assertTrue(Dart.DOUBLE_11.isDouble())
        assertTrue(Dart.DOUBLE_12.isDouble())
        assertTrue(Dart.DOUBLE_13.isDouble())
        assertTrue(Dart.DOUBLE_14.isDouble())
        assertTrue(Dart.DOUBLE_15.isDouble())
        assertTrue(Dart.DOUBLE_16.isDouble())
        assertTrue(Dart.DOUBLE_17.isDouble())
        assertTrue(Dart.DOUBLE_18.isDouble())
        assertTrue(Dart.DOUBLE_19.isDouble())
        assertTrue(Dart.DOUBLE_20.isDouble())
    }

    @Test
    fun `it should correctly determine that singles are not doubles`() {
        assertFalse(Dart.SINGLE_1.isDouble())
        assertFalse(Dart.SINGLE_2.isDouble())
        assertFalse(Dart.SINGLE_3.isDouble())
        assertFalse(Dart.SINGLE_4.isDouble())
        assertFalse(Dart.SINGLE_5.isDouble())
        assertFalse(Dart.SINGLE_6.isDouble())
        assertFalse(Dart.SINGLE_7.isDouble())
        assertFalse(Dart.SINGLE_8.isDouble())
        assertFalse(Dart.SINGLE_9.isDouble())
        assertFalse(Dart.SINGLE_10.isDouble())
        assertFalse(Dart.SINGLE_11.isDouble())
        assertFalse(Dart.SINGLE_12.isDouble())
        assertFalse(Dart.SINGLE_13.isDouble())
        assertFalse(Dart.SINGLE_14.isDouble())
        assertFalse(Dart.SINGLE_15.isDouble())
        assertFalse(Dart.SINGLE_16.isDouble())
        assertFalse(Dart.SINGLE_17.isDouble())
        assertFalse(Dart.SINGLE_18.isDouble())
        assertFalse(Dart.SINGLE_19.isDouble())
        assertFalse(Dart.SINGLE_20.isDouble())
    }

    @Test
    fun `it should correctly determine that triples are not doubles`() {
        assertFalse(Dart.TRIPLE_1.isDouble())
        assertFalse(Dart.TRIPLE_2.isDouble())
        assertFalse(Dart.TRIPLE_3.isDouble())
        assertFalse(Dart.TRIPLE_4.isDouble())
        assertFalse(Dart.TRIPLE_5.isDouble())
        assertFalse(Dart.TRIPLE_6.isDouble())
        assertFalse(Dart.TRIPLE_7.isDouble())
        assertFalse(Dart.TRIPLE_8.isDouble())
        assertFalse(Dart.TRIPLE_9.isDouble())
        assertFalse(Dart.TRIPLE_10.isDouble())
        assertFalse(Dart.TRIPLE_11.isDouble())
        assertFalse(Dart.TRIPLE_12.isDouble())
        assertFalse(Dart.TRIPLE_13.isDouble())
        assertFalse(Dart.TRIPLE_14.isDouble())
        assertFalse(Dart.TRIPLE_15.isDouble())
        assertFalse(Dart.TRIPLE_16.isDouble())
        assertFalse(Dart.TRIPLE_17.isDouble())
        assertFalse(Dart.TRIPLE_18.isDouble())
        assertFalse(Dart.TRIPLE_19.isDouble())
        assertFalse(Dart.TRIPLE_20.isDouble())
    }

    @Test
    fun `it should mark BULL as valid Double`() {
        assertTrue(Dart.BULL.isDouble())
    }

    @Test
    fun `it should NOT mark S_BULL as valid Double`() {
        assertFalse(Dart.SINGLE_BULL.isDouble())
    }

    @Test
    fun `it should create Dart from String`() {
        assertEquals(Dart.NONE, Dart.fromString("this is not a valid darts string"))
    }

    @Test
    fun `it should create Dart from Int when multiplier is 1`() {
        assertEquals(Dart.ZERO, Dart.fromInt(0, 1))
        assertEquals(Dart.SINGLE_1, Dart.fromInt(1, 1))
        assertEquals(Dart.SINGLE_2, Dart.fromInt(2,1))
        assertEquals(Dart.SINGLE_3, Dart.fromInt(3,1))
        assertEquals(Dart.SINGLE_4, Dart.fromInt(4,1))
        assertEquals(Dart.SINGLE_5, Dart.fromInt(5,1))
        assertEquals(Dart.SINGLE_6, Dart.fromInt(6,1))
        assertEquals(Dart.SINGLE_7, Dart.fromInt(7,1))
        assertEquals(Dart.SINGLE_8, Dart.fromInt(8,1))
        assertEquals(Dart.SINGLE_9, Dart.fromInt(9,1))
        assertEquals(Dart.SINGLE_10, Dart.fromInt(10, 1))
        assertEquals(Dart.SINGLE_11, Dart.fromInt(11, 1))
        assertEquals(Dart.SINGLE_12, Dart.fromInt(12, 1))
        assertEquals(Dart.SINGLE_13, Dart.fromInt(13, 1))
        assertEquals(Dart.SINGLE_14, Dart.fromInt(14, 1))
        assertEquals(Dart.SINGLE_15, Dart.fromInt(15, 1))
        assertEquals(Dart.SINGLE_16, Dart.fromInt(16, 1))
        assertEquals(Dart.SINGLE_17, Dart.fromInt(17, 1))
        assertEquals(Dart.SINGLE_18, Dart.fromInt(18, 1))
        assertEquals(Dart.SINGLE_19, Dart.fromInt(19, 1))
        assertEquals(Dart.SINGLE_20, Dart.fromInt(20, 1))
        assertEquals(Dart.SINGLE_BULL, Dart.fromInt(25, 1))
    }

    @Test
    fun `it should create Dart from Int when multiplier is 2`() {
        assertEquals(Dart.ZERO, Dart.fromInt(0, 2))
        assertEquals(Dart.DOUBLE_1, Dart.fromInt(1, 2))
        assertEquals(Dart.DOUBLE_2, Dart.fromInt(2,2))
        assertEquals(Dart.DOUBLE_3, Dart.fromInt(3,2))
        assertEquals(Dart.DOUBLE_4, Dart.fromInt(4,2))
        assertEquals(Dart.DOUBLE_5, Dart.fromInt(5,2))
        assertEquals(Dart.DOUBLE_6, Dart.fromInt(6,2))
        assertEquals(Dart.DOUBLE_7, Dart.fromInt(7,2))
        assertEquals(Dart.DOUBLE_8, Dart.fromInt(8,2))
        assertEquals(Dart.DOUBLE_9, Dart.fromInt(9,2))
        assertEquals(Dart.DOUBLE_10, Dart.fromInt(10, 2))
        assertEquals(Dart.DOUBLE_11, Dart.fromInt(11, 2))
        assertEquals(Dart.DOUBLE_12, Dart.fromInt(12, 2))
        assertEquals(Dart.DOUBLE_13, Dart.fromInt(13, 2))
        assertEquals(Dart.DOUBLE_14, Dart.fromInt(14, 2))
        assertEquals(Dart.DOUBLE_15, Dart.fromInt(15, 2))
        assertEquals(Dart.DOUBLE_16, Dart.fromInt(16, 2))
        assertEquals(Dart.DOUBLE_17, Dart.fromInt(17, 2))
        assertEquals(Dart.DOUBLE_18, Dart.fromInt(18, 2))
        assertEquals(Dart.DOUBLE_19, Dart.fromInt(19, 2))
        assertEquals(Dart.DOUBLE_20, Dart.fromInt(20, 2))
        assertEquals(Dart.BULL, Dart.fromInt(25, 2))
    }

    @Test
    fun `it should create Dart from Int when multiplier is 3`() {
        assertEquals(Dart.ZERO, Dart.fromInt(0, 3))
        assertEquals(Dart.TRIPLE_1, Dart.fromInt(1, 3))
        assertEquals(Dart.TRIPLE_2, Dart.fromInt(2,3))
        assertEquals(Dart.TRIPLE_3, Dart.fromInt(3,3))
        assertEquals(Dart.TRIPLE_4, Dart.fromInt(4,3))
        assertEquals(Dart.TRIPLE_5, Dart.fromInt(5,3))
        assertEquals(Dart.TRIPLE_6, Dart.fromInt(6,3))
        assertEquals(Dart.TRIPLE_7, Dart.fromInt(7,3))
        assertEquals(Dart.TRIPLE_8, Dart.fromInt(8,3))
        assertEquals(Dart.TRIPLE_9, Dart.fromInt(9,3))
        assertEquals(Dart.TRIPLE_10, Dart.fromInt(10, 3))
        assertEquals(Dart.TRIPLE_11, Dart.fromInt(11, 3))
        assertEquals(Dart.TRIPLE_12, Dart.fromInt(12, 3))
        assertEquals(Dart.TRIPLE_13, Dart.fromInt(13, 3))
        assertEquals(Dart.TRIPLE_14, Dart.fromInt(14, 3))
        assertEquals(Dart.TRIPLE_15, Dart.fromInt(15, 3))
        assertEquals(Dart.TRIPLE_16, Dart.fromInt(16, 3))
        assertEquals(Dart.TRIPLE_17, Dart.fromInt(17, 3))
        assertEquals(Dart.TRIPLE_18, Dart.fromInt(18, 3))
        assertEquals(Dart.TRIPLE_19, Dart.fromInt(19, 3))
        assertEquals(Dart.TRIPLE_20, Dart.fromInt(20, 3))
    }

    @Test
    fun `it should create Dart from Int when multiplier is 0`() {
        assertEquals(Dart.NONE, Dart.fromInt(0, 0))
        assertEquals(Dart.NONE, Dart.fromInt(1, 0))
        assertEquals(Dart.NONE, Dart.fromInt(2,0))
        assertEquals(Dart.NONE, Dart.fromInt(3,0))
        assertEquals(Dart.NONE, Dart.fromInt(4,0))
        assertEquals(Dart.NONE, Dart.fromInt(5,0))
        assertEquals(Dart.NONE, Dart.fromInt(6,0))
        assertEquals(Dart.NONE, Dart.fromInt(7,0))
        assertEquals(Dart.NONE, Dart.fromInt(8,0))
        assertEquals(Dart.NONE, Dart.fromInt(9,0))
        assertEquals(Dart.NONE, Dart.fromInt(10, 0))
        assertEquals(Dart.NONE, Dart.fromInt(11, 0))
        assertEquals(Dart.NONE, Dart.fromInt(12, 0))
        assertEquals(Dart.NONE, Dart.fromInt(13, 0))
        assertEquals(Dart.NONE, Dart.fromInt(14, 0))
        assertEquals(Dart.NONE, Dart.fromInt(15, 0))
        assertEquals(Dart.NONE, Dart.fromInt(16, 0))
        assertEquals(Dart.NONE, Dart.fromInt(17, 0))
        assertEquals(Dart.NONE, Dart.fromInt(18, 0))
        assertEquals(Dart.NONE, Dart.fromInt(19, 0))
        assertEquals(Dart.NONE, Dart.fromInt(20, 0))
    }
}