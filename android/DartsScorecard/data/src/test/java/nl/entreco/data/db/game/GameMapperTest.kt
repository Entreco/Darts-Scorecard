package nl.entreco.data.db.game

import nl.entreco.domain.model.Game
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Created by Entreco on 17/12/2017.
 */
class GameMapperTest {

    private val subject = GameMapper()
    private lateinit var givenTabel: GameTable
    private lateinit var actualGame: Game

    @Test
    fun `should create Game`() {
        givenGameTable()
        whenConverting()
        assertNotNull(actualGame)
    }

    @Test
    fun `should create Game with all values set (except teams)`() {
        givenGameTable(id = 1, teams = "1,2|3,4", numLegs = 100, numSets = 1000, startScore = 42, startIndex = 18)
        whenConverting()
        assertEquals(1, actualGame.id)
    }

    private fun givenGameTable(id: Long = 0, teams: String = "", numLegs: Int = 0, numSets: Int = 0, startScore: Int = 0, startIndex: Int = 0) {
        val table = GameTable()
        table.id = id
        table.teams = teams
        table.numLegs = numLegs
        table.numSets = numSets
        table.startScore = startScore
        table.startIndex = startIndex
        givenTabel = table
    }

    private fun whenConverting() {
        actualGame = subject.to(givenTabel)
    }
}