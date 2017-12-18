package nl.entreco.domain.model

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.TurnHandler
import org.junit.Assert.assertEquals
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
    private val teams = arrayOf(Team(arrayOf(player1)), Team(arrayOf(player2, player3)), Team(arrayOf(player4, player5)))
    private val startIndex = 0

    private var subject = TurnHandler(startIndex, teams)

    private var scores = arrayOf(Score(), Score(), Score(), Score(), Score())

    @Test
    fun `player1 should start`() {
        assertEquals(player1, givenStartedGame())
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw illegalState when calling next without start`() {
        assertEquals(player2, subject.next(scores))
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
        assertEquals(player2, subject.nextLeg(scores).player)
        assertEquals(player4, subject.nextLeg(scores).player)
        assertEquals(player1, subject.nextLeg(scores).player)
        assertEquals(player3, subject.nextLeg(scores).player)
        assertEquals(player5, subject.nextLeg(scores).player)
    }

    @Test
    fun `it should correctly determine set starters`() {
        givenStartedGame()
        assertEquals(player2, subject.nextSet(scores).player)
        assertEquals(player4, subject.nextSet(scores).player)
        assertEquals(player1, subject.nextSet(scores).player)
        assertEquals(player3, subject.nextSet(scores).player)
        assertEquals(player5, subject.nextSet(scores).player)
    }

    @Test
    fun `complete game`() {
        givenStartedGame()
        assertEquals(player2, subject.nextLeg(scores).player)
        assertEquals(player4, subject.nextLeg(scores).player)
        assertEquals(player1, subject.nextLeg(scores).player)
        assertEquals(player3, subject.nextSet(scores).player)
        assertEquals(player5, subject.nextLeg(scores).player)
        assertEquals(player1, subject.nextLeg(scores).player)
        assertEquals(player4, subject.nextSet(scores).player)
    }

    private fun givenStartedGame(): Player {
        return subject.start().player
    }

    private fun playFirstRound() {
        assertEquals(player2, subject.next(scores).player)
        assertEquals(player4, subject.next(scores).player)
        assertEquals(player1, subject.next(scores).player)
    }

    private fun playSecondRound() {
        assertEquals(player3, subject.next(scores).player)
        assertEquals(player5, subject.next(scores).player)
        assertEquals(player1, subject.next(scores).player)
    }
}