package nl.entreco.domain.play

import nl.entreco.domain.play.Arbiter
import nl.entreco.domain.play.Score
import nl.entreco.domain.play.Turn
import org.junit.Test

import org.junit.Assert.*

class ArbiterTest {

    private val subject: Arbiter = Arbiter(initial = Score(), numPlayers = 2)

    @Test
    fun `it should not start new leg if zero is scored`() {
        subject.handle(Turn(0, 0, 0), 0)

        assertEquals(0, subject.getLegs().size)
    }

    @Test
    fun `it should handle leg finished`() {
        givenLegFinished()

        assertEquals(Score(501, 1, 0), subject.getScores()[0])
        assertEquals(1, subject.getLegs().size)
        assertEquals(0, subject.getSets().size)
    }

    @Test
    fun `it should handle set finished`() {
        givenSetFinished()

        assertEquals(Score(501, 0, 1), subject.getScores()[0])
        assertEquals(3, subject.getLegs().size)
        assertEquals(1, subject.getSets().size)
    }

    @Test
    fun `it should handle match finished`() {
        givenSetFinished()
        givenSetFinished()

        assertEquals(Score(501, 0, 2), subject.getScores()[0])
        assertEquals(6, subject.getLegs().size)
        assertEquals(2, subject.getSets().size)
    }

    private fun givenSetFinished() {
        givenLegFinished()
        givenLegFinished()
        givenLegFinished()
    }

    private fun givenLegFinished() {
        subject.handle(Turn(501, 0, 0), 0)
    }
}