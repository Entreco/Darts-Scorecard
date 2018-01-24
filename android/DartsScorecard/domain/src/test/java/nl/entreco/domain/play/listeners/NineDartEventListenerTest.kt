package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.listeners.events.NineDartEvent
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean


class NineDartEventListenerTest : SpecialEventListenerTest() {

    @Test
    fun `it should notify about NineDart event when (180, 321)`() {
        val subject = NineDartEventListener()
        subject.onScored(`180`(), 321)
        assertTrue(subject.wasPossible())
    }

    @Test
    fun `it should notify about NineDart event when (177, 324)`() {
        val subject = NineDartEventListener()
        subject.onScored(`177`(), 324)
        assertTrue(subject.wasPossible())
    }

    @Test
    fun `shortCut for all possibilities`() {
        shouldBePossible(`180`(), `180`())
        shouldBePossible(`180`(), `171`())
        shouldBePossible(`171`(), `180`())
        shouldBePossible(`177`(), `180`())
        shouldBePossible(`180`(), `177`())
        shouldBePossible(`177`(), `177`())
        shouldBePossible(`167`(), `167`())
    }

    private fun shouldBePossible(vararg turns: Turn) {
        val subject = NineDartEventListener()
        var score = 501
        turns.forEach {
            score -= it.total()
            subject.onScored(it, score)
            assertTrue("not possible score:$score with turns: $turns", subject.wasPossible())
        }
    }

    @Test
    fun `it should NOT notify about NineDart event when (60, 321)`() {
        val subject = NineDartEventListener()
        subject.onScored(`60`(), 321)
        assertFalse(subject.wasPossible())
    }

    private class NineDartEventListener(private val check: AtomicBoolean = AtomicBoolean(false)) : SpecialEventListener<NineDartEvent> {
        private val isPossible = AtomicBoolean(false)
        private val player = Player("Nine Dart Master")

        override fun handle(event: NineDartEvent) {
            check.set(true)
            isPossible.set(event.isPossible())
        }

        fun onScored(turn: Turn, score: Int) {
            val scores = arrayOf(Score(score))
            val team = Team(arrayOf(player))
            val next = Next(State.NORMAL, team, 0, player, scores[0])
            onSpecialEvent(next, turn, player, scores)
        }

        fun wasPossible(): Boolean {
            return isPossible.get()
        }
    }
}