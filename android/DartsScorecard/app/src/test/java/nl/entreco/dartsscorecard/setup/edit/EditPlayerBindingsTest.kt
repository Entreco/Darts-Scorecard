package nl.entreco.dartsscorecard.setup.edit

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.dartsscorecard.databinding.ExistingPlayerViewBinding
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

    @Mock
    private lateinit var mockViewGroup: ViewGroup
    @Mock
    private lateinit var mockRoot: View
    @Mock
    private lateinit var mockTextView: TextView
    @Mock
    private lateinit var mockClicker: ExistingPlayerSelectedClicker
    @Mock
    private lateinit var mockBinding: ExistingPlayerViewBinding

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

    @Test
    fun `it should select all on Focus`() {
        EditPlayerBindings.selectAll(mockTextView, "Henk")
        verify(mockTextView).text = "Henk"
        verify(mockTextView).clearFocus()
        verify(mockTextView).setSelectAllOnFocus(true)
        verify(mockTextView).requestFocusFromTouch()
    }

    private fun givenPlayer(name: String): Player {
        whenever(mockBinding.root).thenReturn(mockRoot)
        return Player(name)
    }
}