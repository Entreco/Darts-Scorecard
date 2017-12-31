package nl.entreco.dartsscorecard.setup.players

import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.databinding.SelectPlayerViewBinding
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class SelectPlayerViewTest {
    @Mock private lateinit var mockBinding : SelectPlayerViewBinding
    @Mock private lateinit var mockView : View
    @Mock private lateinit var mockViewModel : PlayerViewModel
    private lateinit var subject : SelectPlayerView

    @Before
    fun setUp() {
        whenever(mockBinding.root).thenReturn(mockView)
        subject = SelectPlayerView(mockBinding)
    }

    @Test
    fun bind() {
        subject.bind(mockViewModel)
        verify(mockBinding).player = mockViewModel
        verify(mockBinding).executePendingBindings()
    }

}