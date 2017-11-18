package nl.entreco.domain.play.model.players

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 18/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamTest{

    val player1 = Player("one")
    val player2 = Player("two")

    val subject : Team = Team(listOf(player1, player2))

    @Test
    fun `it should have non null list of players`() {
        assertNotNull(subject.players)
    }

    @Test
    fun `it should have first player initially`() {
        assertEquals(player1, subject.currentPlayer)
    }

    @Test
    fun `it should have player2 after 1 turn`() {
        assertEquals(player2, subject.next())
    }

    @Test
    fun `it should have player1 after 2 turns`() {
        subject.next()
        subject.next()
        assertEquals(player1, subject.currentPlayer)
    }

    @Test
    fun `it should have player2 after 3 turns`() {
        subject.next()
        subject.next()
        subject.next()
        assertEquals(player2, subject.currentPlayer)
    }

    @Test
    fun `it should print all players correct`() {
        assertEquals("one & two", subject.toString())
    }
}