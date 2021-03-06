package nl.entreco.domain.model.players

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 18/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamTest {

    private val player1 = Player("one")
    private val player2 = Player("two")
    private val player3 = Player("three")

    val subject: Team = Team(arrayOf(player1, player2))

    @Test
    fun `it should have non null list of players`() {
        assertNotNull(subject.players)
    }

    @Test
    fun `it should print all players correct`() {
        assertEquals("one & two", subject.toString())
    }

    @Test
    fun `it should contain player1`() {
        assertTrue(subject.contains(player1))
    }

    @Test
    fun `it should contain player2`() {
        assertTrue(subject.contains(player2))
    }

    @Test
    fun `it should not contain player3`() {
        assertFalse(subject.contains(player3))
    }

    @Test
    fun `it should contain player with id 0`() {
        assertTrue(subject.contains(0L))
    }

    @Test
    fun `it should NOT contain player with id 1`() {
        assertFalse(subject.contains(1L))
    }

    @Test
    fun `it should have image initially`() {
        assertEquals("team2", subject.imageUrl())
    }

    @Test
    fun `it should print player Ids`() {
        assertEquals("0,0", subject.toTeamString())
    }
}