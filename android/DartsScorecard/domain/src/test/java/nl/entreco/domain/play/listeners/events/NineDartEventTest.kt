package nl.entreco.domain.play.listeners.events

import org.mockito.kotlin.verifyZeroInteractions
import nl.entreco.domain.model.players.Player
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class NineDartEventTest {
    @Mock private lateinit var mockBy : Player
    private lateinit var subject: NineDartEvent

    @Test
    fun `everything is possible`() {
        givenSubject(true)
        assertTrue(subject.isPossible())
    }

    @Test
    fun `except when isPossible is false`() {
        givenSubject(false)
        assertFalse(subject.isPossible())
    }

    @Test
    fun `should not interact with player`() {
        givenSubject(true)
        verifyZeroInteractions(mockBy)
    }

    @Test
    fun `should return player`() {
        givenSubject(false)
        assertEquals(mockBy, subject.by())
    }

    private fun givenSubject(isPossible: Boolean) {
        subject = NineDartEvent(isPossible, mockBy)
    }

}