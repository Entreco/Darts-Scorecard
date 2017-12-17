package nl.entreco.data.db.game

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class GameMapperTest {

    private val subject = GameMapper()

    @Test
    fun `should create Game`() {
        val table = givenTable()
        assertNotNull(subject.to(table))
    }

    @Test
    fun `should create Game with all values set (except teams)`() {
        val table = givenTable(id = 1, teams = "1,2|3,4", numLegs = 100, numSets = 1000, startScore = 42, startIndex = 18)
        val actual = subject.to(table)
        assertEquals(1, actual.id)
        assertEquals(100, actual.arbiter.getScoreSettings().numLegs)
        assertEquals(1000, actual.arbiter.getScoreSettings().numSets)
        assertEquals(42, actual.arbiter.getScoreSettings().startScore)
        assertEquals(18, actual.arbiter.getScoreSettings().teamStartIndex)
    }

    @Test
    fun `should create Game without teams (should be set later)`() {
        val table = givenTable(id = 1, teams = "1,2|3,4", numLegs = 100, numSets = 1000, startScore = 42, startIndex = 18)
        val actual = subject.to(table)
        assertEquals(0, actual.teams().size)
        assertArrayEquals(emptyArray(), actual.teams())
    }

    private fun givenTable(id: Long = 0, teams: String = "", numLegs: Int = 0, numSets: Int = 0, startScore: Int = 0, startIndex: Int = 0): GameTable {
        val table = GameTable()
        table.id = id
        table.teams = teams
        table.numLegs = numLegs
        table.numSets = numSets
        table.startScore = startScore
        table.startIndex = startIndex
        return table
    }
}