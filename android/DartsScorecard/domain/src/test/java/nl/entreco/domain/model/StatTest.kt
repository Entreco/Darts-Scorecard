package nl.entreco.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by entreco on 23/01/2018.
 */
class StatTest {

    @Test
    fun `it should handle adding nulls`() {
        val stat = statWith()
        assertEquals(stat, stat + null)
    }

    @Test
    fun `it should handle being added to null`() {
        val stat = statWith()
        assertNotEquals(null, null + stat)
        assertNotEquals(stat, null + stat)
    }

    @Test
    fun `it should add totalScore-stats together`() {
        assertEquals(42, totalScore(20, 20, 2).totalScore)
    }

    @Test
    fun `it should add numDarts-stats together`() {
        assertEquals(32, numDarts(20, 10, 2).nDarts)
    }

    @Test
    fun `it should add 180s-stats together`() {
        assertEquals(2, n180s(-1, 1, 2).n180)
    }

    @Test
    fun `it should add 140-stats together`() {
        assertEquals(6, n140s(1, 2, 3).n140)
    }

    @Test
    fun `it should add 100-stats together`() {
        assertEquals(3, n100s(1, 1, 1).n100)
    }

    private fun totalScore(score: Int, vararg scores: Int): Stat {
        var stat = statWith(totalScore = score)
        scores.map { statWith(totalScore = it) }.forEach { stat += it }
        return stat
    }

    private fun numDarts(dart: Int, vararg darts: Int): Stat {
        var stat = statWith(nDarts = dart)
        darts.map { statWith(nDarts = it) }.forEach { stat += it }
        return stat
    }

    private fun n180s(dart: Int, vararg darts: Int): Stat {
        var stat = statWith(n180 = dart)
        darts.map { statWith(n180 = it) }.forEach { stat += it }
        return stat
    }

    private fun n140s(dart: Int, vararg darts: Int): Stat {
        var stat = statWith(n140 = dart)
        darts.map { statWith(n140 = it) }.forEach { stat += it }
        return stat
    }

    private fun n100s(dart: Int, vararg darts: Int): Stat {
        var stat = statWith(n100 = dart)
        darts.map { statWith(n100 = it) }.forEach { stat += it }
        return stat
    }

    private fun statWith(playerId: Long = 0, totalScore: Int = 0, nDarts: Int = 0,
                         n180: Int = 0, n140: Int = 0, n100: Int = 0, nAtCheckout: Int = 0, nCheckouts: Int = 0,
                         nBreaks: Int = 0, highest: List<Int> = emptyList(), highestCo: List<Int> = emptyList()): Stat {
        return Stat(playerId, totalScore, nDarts, n180, n140, n100, nAtCheckout, nCheckouts, nBreaks, highest, highestCo)
    }
}