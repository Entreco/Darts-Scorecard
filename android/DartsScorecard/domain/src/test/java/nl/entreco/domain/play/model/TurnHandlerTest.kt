package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandlerTest {

    private val player1 = Player("1")
    private val player2 = Player("2")
    private val player3 = Player("3")
    private val player4 = Player("4")
    private val player5 = Player("5")

    private var subject = TurnHandler(Team(player1), Team(player2, player3), Team(player4, player5))

    @Test
    fun `player1 should start`() {
        assertEquals(player1, givenStartedGame())
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw illegalState when calling next without start`() {
        assertEquals(player2, subject.next())
    }

    @Test
    fun `all 1st players of a team should go first`() {
        givenStartedGame()
        playFirstRound()
    }

    @Test
    fun `all 2nd players of a team should go after`() {
        givenStartedGame()
        playFirstRound()
        playSecondRound()
    }

    @Test
    fun `all 1st players of a team should go after`() {
        givenStartedGame()
        playFirstRound()
        playSecondRound()
        playFirstRound()
    }

    @Test
    fun `it should correctly determine leg starters`() {
        givenStartedGame()
        assertEquals(player2, subject.nextLeg())
        assertEquals(player4, subject.nextLeg())
        assertEquals(player1, subject.nextLeg())
        assertEquals(player3, subject.nextLeg())
        assertEquals(player5, subject.nextLeg())
    }

    @Test
    fun `it should correctly determine set starters`() {
        givenStartedGame()
        assertEquals(player2, subject.nextSet())
        assertEquals(player4, subject.nextSet())
        assertEquals(player1, subject.nextSet())
        assertEquals(player3, subject.nextSet())
        assertEquals(player5, subject.nextSet())
    }

    @Test
    fun `complete game`() {
        givenStartedGame()
        assertEquals(player2, subject.nextLeg())
        assertEquals(player4, subject.nextLeg())
        assertEquals(player1, subject.nextLeg())
        assertEquals(player3, subject.nextSet())
        assertEquals(player5, subject.nextLeg())
        assertEquals(player1, subject.nextLeg())
        assertEquals(player4, subject.nextSet())
    }

    private fun givenStartedGame() : Player {
        return subject.start()
    }

    private fun playFirstRound() {
        assertEquals(player2, subject.next())
        assertEquals(player4, subject.next())
        assertEquals(player1, subject.next())
    }

    private fun playSecondRound() {
        assertEquals(player3, subject.next())
        assertEquals(player5, subject.next())
        assertEquals(player1, subject.next())
    }
}