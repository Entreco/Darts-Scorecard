package nl.entreco.domain.model

import nl.entreco.domain.TestProvider
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.Arbiter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Created by Entreco on 05/12/2017.
 */
class GameWithBustTest {

    lateinit var subject: Game

    private val teams: Array<Team> = TestProvider().teams()
    private val startIndex: Int = TestProvider().startIndex()
    private val arbiter: Arbiter = Arbiter(Score())

    @Test
    fun `it should update next to State ERR_BUST when bust scored`() {
        val expected = givenStartedGame()
        whenSubmittingBust()
        verifyNextChangedTo(expected, subject.next, State.ERR_BUST)
    }

    @Test
    fun `it should update next to State NORMAL when normal score scored`() {
        val expected = givenStartedGame()
        whenSubmittingNormalScore()
        verifyNextChangedTo(expected, subject.next, State.NORMAL)
    }

    private fun givenStartedGame(): Next {
        subject = Game(1, arbiter)
        subject.start(startIndex, teams)
        return subject.next
    }

    private fun whenSubmittingBust() {
        subject.handle(Turn(Dart.TEST_501, Dart.TEST_501, Dart.TEST_501))
    }

    private fun whenSubmittingNormalScore() {
        subject.handle(Turn(Dart.SINGLE_1, Dart.DOUBLE_20, Dart.SINGLE_1))
    }

    private fun verifyNextChangedTo(invalid: Next, next: Next, expectedState: State) {
        assertNotEquals(invalid, next)
        assertEquals(expectedState, next.state)
    }
}