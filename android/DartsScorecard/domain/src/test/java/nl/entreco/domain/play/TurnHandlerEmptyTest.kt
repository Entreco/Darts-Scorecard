package nl.entreco.domain.play

import nl.entreco.domain.TestProvider
import nl.entreco.domain.model.players.NoPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandlerEmptyTest {

    private val teamDoesNotExist = Team(arrayOf(Player("nono")))
    private val team = Team(arrayOf(Player("henkie")))
    private val teams = arrayOf(team)
    private val subject = TurnHandler(0, teams)

    @Test
    fun `it should NOT throw exception if teams are not set and calling start()`() {
        subject.start()
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling next()`() {
        subject.next(emptyArray())
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling nextLeg()`() {
        subject.nextLeg(emptyArray())
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling nextSet()`() {
        subject.nextSet(emptyArray())
    }

    @Test
    fun `it should return NoPlayer initially`() {
        assertEquals(NoPlayer().toString(), TestProvider().turnHandler().toString())
    }

    @Test
    fun `it should return NoPlayer after starting with no teams`() {
        assertNotEquals(NoPlayer(), TestProvider().turnHandler().start().player)
    }

    @Test
    fun `it should return correct index of Team`() {
        assertEquals(0, subject.indexOf(team))
    }

    @Test
    fun `it should return correct index of Team that is not participating`() {
        assertEquals(-1, subject.indexOf(teamDoesNotExist))
    }
}