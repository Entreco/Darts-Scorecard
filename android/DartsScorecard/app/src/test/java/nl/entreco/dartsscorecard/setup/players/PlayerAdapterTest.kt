package nl.entreco.dartsscorecard.setup.players

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayerAdapterTest {

    private val subject = PlayerAdapter()

    @Test
    fun `it should add 1 player by default to playersMap`() {
        assertEquals(1, subject.itemCount)
        assertEquals(1, subject.playersMap().size)
    }

    @Test
    fun `it should add new player`() {
        subject.onAddPlayer()
        assertEquals(2, subject.itemCount)
        assertEquals(2, subject.playersMap().size)
    }

}