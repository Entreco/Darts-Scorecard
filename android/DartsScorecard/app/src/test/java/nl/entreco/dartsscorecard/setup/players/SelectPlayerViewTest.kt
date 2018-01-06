package nl.entreco.dartsscorecard.setup.players

import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class SelectPlayerViewTest {
    @Mock private lateinit var mockBinding: SelectPlayerViewBinding
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockEntries: MutableList<Int>
    @Mock private lateinit var mockEditor: PlayerEditor
    @Mock private lateinit var mockPlayerViewModel: PlayerViewModel
    private val mockPositionInList = 42
    private lateinit var subject: SelectPlayerView

    @Before
    fun setUp() {
        whenever(mockBinding.root).thenReturn(mockView)
        subject = SelectPlayerView(mockBinding)
    }

    @Test
    fun `it should bind playerViewModel`() {
        whenBinding()
        verify(mockBinding).player = mockPlayerViewModel
    }

    @Test
    fun `it should bind editor`() {
        whenBinding()
        verify(mockBinding).editor = mockEditor
    }

    @Test
    fun `it should bind entries`() {
        whenBinding()
        verify(mockBinding).entries = mockEntries
    }

    @Test
    fun `it should bind positionInList`() {
        whenBinding()
        verify(mockBinding).positionInList = mockPositionInList
    }

    @Test
    fun `it should executePendingBindings`() {
        whenBinding()
        verify(mockBinding).executePendingBindings()
    }

    private fun whenBinding() {
        subject.bind(mockPlayerViewModel, mockEditor, mockEntries, mockPositionInList)
    }
}