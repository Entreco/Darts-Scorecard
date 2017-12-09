package nl.entreco.domain.play.model

import nl.entreco.domain.play.TestProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArbiterTest {

    private val tp = TestProvider()
    private var turnHandler: TurnHandler = tp.turnHandler()
    private var next: Next = tp.next()
    private lateinit var subject: Arbiter

    @Before
    fun setUp() {
        subject = Arbiter(Score(), turnHandler).apply { start() }
    }

    @Test
    fun `it should not start new leg if zero is scored`() {
        subject.handle(Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO), next)

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
        subject.handle(Turn(Dart.SINGLE_1, Dart.TEST_D250), next)
    }
}