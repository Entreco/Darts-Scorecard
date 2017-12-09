package nl.entreco.domain.play.listeners

import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Entreco on 05/12/2017.
 */
class NoScoreEventListenerTest : SpecialEventListenerTest() {

    @Test
    fun `it should notify about NoScore event when NoScore was scored`() {
        val subject = NoScoreEventListener()
        subject.onScored(`No Score`())
        Assert.assertTrue(subject.wasNotified())
    }

    @Test
    fun `it should not notify about 180 event when 180 was scored`() {
        val subject = NoScoreEventListener()
        subject.onScored(`180`())
        Assert.assertFalse(subject.wasNotified())
    }

    @Test
    fun `it should not notify about 180 event when no 180 was scored`() {
        val subject = NoScoreEventListener()
        subject.onScored(`60`())
        Assert.assertFalse(subject.wasNotified())
    }

    private class NoScoreEventListener(private val check: AtomicBoolean = AtomicBoolean(false)) : SpecialEventListener<NoScoreEvent> {

        private val player = Player("No Score thrower")
        private val team = Team(player)

        override fun handle(event: NoScoreEvent) {
            check.set(event.noScore)
        }

        fun onScored(turn: Turn) {
            onSpecialEvent(Next(State.NORMAL, team, 0, player), turn, player, arrayOf(Score()))
        }

        fun wasNotified(): Boolean {
            return check.get()
        }
    }
}