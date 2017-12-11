package nl.entreco.domain.play.listeners

import nl.entreco.domain.play.listeners.events.OneEightyEvent
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

class OneEightyEventListenerTest : SpecialEventListenerTest() {
    @Test
    fun `it should notify about 180 event when 180 was scored`() {
        val subject = OneEightEventListener()
        subject.onScored(`180`())
        Assert.assertTrue(subject.wasNotified())
    }

    @Test
    fun `it should not notify about 180 event when no 180 was scored`() {
        val subject = OneEightEventListener()
        subject.onScored(`60`())
        Assert.assertFalse(subject.wasNotified())
    }

    @Test
    fun `it should not notify about NoScore event when NoScore was scored`() {
        val subject = OneEightEventListener()
        subject.onScored(`No Score`())
        Assert.assertFalse(subject.wasNotified())
    }

    private class OneEightEventListener(private val check: AtomicBoolean = AtomicBoolean(false)) : SpecialEventListener<OneEightyEvent> {

        private val player = Player("180 thrower")
        private val team = Team(player)
        private val score = Score()

        override fun handle(event: OneEightyEvent) {
            check.set(true)
        }

        fun onScored(turn: Turn) {
            onSpecialEvent(Next(State.NORMAL, team, 0, player, score), turn, player, arrayOf(score))
        }

        fun wasNotified(): Boolean {
            return check.get()
        }
    }
}