package nl.entreco.domain

import org.junit.Test

import org.junit.Assert.*

class ArbiterTest {

    private val subject: Arbiter = Arbiter(startScore = Score(), numPlayers = 2)

    @Test
    fun `it should not start new leg if zero is scored`() {
        subject.handle(Turn(0,0,0), 0)

        assertEquals(0, subject.getLegs().size)
    }

}