package nl.entreco.dartsscorecard.setup.players

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import nl.entreco.dartsscorecard.setup.Setup01Navigator
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayerAdapterTest {

    @Mock private lateinit var mockPlayerViewHolder : SelectPlayerView
    @Mock private lateinit var mockNavigator : Setup01Navigator
    @InjectMocks private lateinit var subject : PlayerAdapter

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

    @Test
    fun `it should bind to correct item`() {
        subject.onBindViewHolder(mockPlayerViewHolder, 0)
        verify(mockPlayerViewHolder).bind(any())
    }

    @Test
    fun `it should NOT bind when item is null`() {
        subject.onBindViewHolder(null, 0)
        verifyZeroInteractions(mockPlayerViewHolder)
    }
}