package nl.entreco.domain.play.model

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
}