package nl.entreco.domain.play.listeners

import nl.entreco.domain.play.listeners.events.BustEvent
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
 * Created by Entreco on 06/12/2017.
 */
class BustEventListenerTest  : SpecialEventListenerTest() {
    @Test
    fun `it should notify about Bust event when next state == BUST`() {
        val subject = BustEventListener()
        subject.onNext(State.ERR_BUST)
        Assert.assertTrue(subject.wasNotified())
    }

    @Test
    fun `it should not notify about Bust event when next state == NORMAL`() {
        val subject = BustEventListener()
        subject.onNext(State.NORMAL)
        Assert.assertFalse(subject.wasNotified())
    }

    @Test
    fun `it should not notify about Bust event when next state == MATCH`() {
        val subject = BustEventListener()
        subject.onNext(State.MATCH)
        Assert.assertFalse(subject.wasNotified())
    }

    private class BustEventListener(private val check: AtomicBoolean = AtomicBoolean(false)) : SpecialEventListener<BustEvent> {

        private val player = Player("No Score thrower")
        private val team = Team(arrayOf(player))
        private val score = Score()

        override fun handle(event: BustEvent) {
            check.set(true)
        }

        fun onNext(state: State) {
            onSpecialEvent(Next(state, team, 0, player, score), Turn(), player, arrayOf(score))
        }

        fun wasNotified(): Boolean {
            return check.get()
        }
    }
}