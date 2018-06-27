package nl.entreco.dartsscorecard.play.live

import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.listeners.events.NineDartEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamScoreListenerTest {

    @Mock private lateinit var mockPlayer: Player
    private val subject = TeamScoreListenerForTesting()

    @Test
    fun `it should call NineDartEvent when nine-darter`() {
        subject.handle(NineDartEvent(true, mockPlayer))
        Assert.assertTrue(subject.nineDartEvent.get())
    }

    @Test
    fun `it should call NineDartEvent when no nine-darter`() {
        subject.handle(NineDartEvent(false, mockPlayer))
        Assert.assertTrue(subject.nineDartEvent.get())
    }

    @Test
    fun `it should not call NineDartEvent when different event`() {
        subject.handle(NoScoreEvent(true))
        Assert.assertFalse(subject.nineDartEvent.get())
    }

    private class TeamScoreListenerForTesting : TeamScoreListener {
        val nineDartEvent = AtomicBoolean(false)

        override fun onNineDartEvent(event: NineDartEvent) {
            nineDartEvent.set(true)
        }
    }
}