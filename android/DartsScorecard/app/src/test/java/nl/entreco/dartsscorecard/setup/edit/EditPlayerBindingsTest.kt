package nl.entreco.dartsscorecard.setup.edit

import android.view.View
import android.view.ViewGroup
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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

    @Mock private lateinit var mockViewGroup: ViewGroup
    @Mock private lateinit var mockRoot: View
    @Mock private lateinit var mockClicker: ExistingPlayerSelectedClicker
    @Mock private lateinit var mockBinding: ExistingPlayerViewBinding

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
    fun `it should set player on Binding`() {
        val expectedPlayer = givenPlayer("Pjotr")

        EditPlayerBindings.bind(mockBinding, expectedPlayer, mockClicker, mockViewGroup)
        verify(mockBinding).player = expectedPlayer
    }

    @Test
    fun `it should set clicker on Binding`() {
        val expectedPlayer = givenPlayer("Pjotr")

        EditPlayerBindings.bind(mockBinding, expectedPlayer, mockClicker, mockViewGroup)
        verify(mockBinding).clicker = mockClicker
    }

    @Test
    fun `it should add view to viewGroup`() {
        val expectedPlayer = givenPlayer("Pjotr")

        EditPlayerBindings.bind(mockBinding, expectedPlayer, mockClicker, mockViewGroup)
        verify(mockViewGroup).addView(mockRoot)
    }

    private fun givenPlayer(name: String): Player {
        whenever(mockBinding.root).thenReturn(mockRoot)
        return Player(name)
    }
}