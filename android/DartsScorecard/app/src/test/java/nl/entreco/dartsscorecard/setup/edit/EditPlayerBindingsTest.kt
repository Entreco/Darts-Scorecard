package nl.entreco.dartsscorecard.setup.edit

import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.model.players.Player
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class EditPlayerBindingsTest {

    @Mock private lateinit var mockViewGroup: ViewGroup
    @Mock private lateinit var mockClicker: ExistingPlayerSelectedClicker

    @Test
    fun `it should remove all previous views from ViewGroup`() {
        val players = emptyList<Player>()
        EditPlayerBindings.showFilteredPlayers(mockViewGroup, players, mockClicker)
        verify(mockViewGroup).removeAllViews()
        verify(mockViewGroup, times(0)).addView(any())
    }

    @Test
    fun `it should not add new views when players is emptyList()`() {
        val players = emptyList<Player>()
        EditPlayerBindings.showFilteredPlayers(mockViewGroup, players, mockClicker)
        verify(mockViewGroup, times(0)).addView(any())
    }
}