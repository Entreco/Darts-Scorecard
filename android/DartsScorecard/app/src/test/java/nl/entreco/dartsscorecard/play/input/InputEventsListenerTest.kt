package nl.entreco.dartsscorecard.play.input

import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Entreco on 10/12/2017.
 */
class InputEventsListenerTest {

    private val subject = InputEventsListenerForTesting()

    @Test
    fun `it should call BustEvent when busting`() {
        subject.handle(BustEvent())
        assertTrue(subject.bustEvent.get())
        assertFalse(subject.scoreEvent.get())
    }

    @Test
    fun `it should call NoScoreEvent when noScoring`() {
        subject.handle(NoScoreEvent(true))
        assertTrue(subject.scoreEvent.get())
        assertFalse(subject.bustEvent.get())
    }

    private class InputEventsListenerForTesting : InputEventsListener {
        val scoreEvent = AtomicBoolean(false)
        val bustEvent = AtomicBoolean(false)

        override fun onNoScoreEvent(event: NoScoreEvent) {
            scoreEvent.set(true)
        }

        override fun onBustEvent(event: BustEvent) {
            bustEvent.set(true)
        }

    }
}