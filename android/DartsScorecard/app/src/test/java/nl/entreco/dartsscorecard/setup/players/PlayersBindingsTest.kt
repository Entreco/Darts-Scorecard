package nl.entreco.dartsscorecard.setup.players

import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class PlayersBindingsTest {

    @Mock private lateinit var mockWidget: PlayersWidget
    @Mock private lateinit var mockAdapter: PlayerAdapter

    @Test
    fun setAdapter() {
        PlayersBindings.setAdapter(mockWidget, mockAdapter)
        verify(mockWidget).adapter = mockAdapter
    }

}