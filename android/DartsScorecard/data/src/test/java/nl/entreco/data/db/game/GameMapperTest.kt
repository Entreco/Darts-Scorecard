package nl.entreco.data.db.game

import nl.entreco.domain.play.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class GameMapperTest {
    private val subject = GameMapper()

    @Test(expected = IllegalStateException::class)
    fun `should throw exception when invalid table`() {
        val table = givenTable()
        subject.to(table)
    }

    @Test
    fun `should create Game when valid table`() {
        val table = givenTable(id = 1, teams = "1,2|3,4", numLegs = 100, numSets = 1000, startScore = 42, startIndex = 18)
        val actual = subject.to(table)
        assertEquals(1, actual.id)
        assertEquals(2, actual.teams().size)
        assertEquals("1", playerName(actual, 0, 0))
        assertEquals("2", playerName(actual, 0, 1))
        assertEquals("3", playerName(actual, 1, 0))
        assertEquals("4", playerName(actual, 1, 1))
        assertEquals(100, actual.arbiter.getScoreSettings().numLegs)
        assertEquals(1000, actual.arbiter.getScoreSettings().numSets)
        assertEquals(42, actual.arbiter.getScoreSettings().startScore)
        assertEquals(18, actual.arbiter.getScoreSettings().teamStartIndex)
    }

    private fun playerName(actual: Game, team: Int, player: Int) =
            actual.teams()[team].players[player].name

    private fun givenTable(id : Long = 0, teams: String = "", numLegs: Int = 0, numSets: Int = 0, startScore: Int = 0, startIndex: Int = 0): GameTable {
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