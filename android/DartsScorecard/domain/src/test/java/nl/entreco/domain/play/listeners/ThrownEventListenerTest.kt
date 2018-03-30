package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.events.ThrownEvent
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Entreco on 10/12/2017.
 */
class ThrownEventListenerTest : SpecialEventListenerTest() {

    @Test
    fun `it should notify about throw when busting`() {
        val subject = ThrownEventListener()
        subject.onThrown(bust())
        Assert.assertTrue(subject.wasNotified())
        Assert.assertEquals("test_501 test_501 test_501", subject.describe())
    }

    private class ThrownEventListener(private val check: AtomicBoolean = AtomicBoolean(false)) : SpecialEventListener<ThrownEvent> {

        private val player = Player("180 thrower")
        private val team = Team(arrayOf(player))
        private val score = Score()
        private var description: String? = null

        override fun handle(event: ThrownEvent) {
            check.set(true)
            description = event.describe
        }

        fun describe(): String? {
            return description
        }

        fun onThrown(turn: Turn) {
            onSpecialEvent(Next(State.NORMAL, team, 0, player, score), turn, player, arrayOf(score))
        }

        fun wasNotified(): Boolean {
            return check.get()
        }
    }
}