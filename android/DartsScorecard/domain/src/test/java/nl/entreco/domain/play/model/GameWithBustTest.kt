package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.Team
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Entreco on 05/12/2017.
 */
class GameWithBustTest {

    lateinit var subject: Game

    private val team: Team = Team("team1")
    private val turnHandler: TurnHandler = TurnHandler(arrayOf(team), 0)
    private val arbiter: Arbiter = Arbiter(Score(), turnHandler)

    @Test
    fun `it should not update next when bust scored`() {
        val expected = givenStartedGame()
        whenSubmittingBust()
        verifyNextDidNotChange(expected, subject.next)
    }

    private fun givenStartedGame(): Next {
        subject = Game(arbiter)
        subject.start()
        return subject.next
    }

    private fun whenSubmittingBust() {
        subject.handle(Turn(Dart.TEST_501, Dart.TEST_501, Dart.TEST_501))
    }

    private fun verifyNextDidNotChange(expected: Next, next: Next) {
        assertEquals(expected, next)
    }
}