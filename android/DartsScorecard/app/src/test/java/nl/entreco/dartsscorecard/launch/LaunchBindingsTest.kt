package nl.entreco.dartsscorecard.launch

import android.view.View
import android.view.ViewPropertyAnimator
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.setup.game.CreateGameResponse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 31/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class LaunchBindingsTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator
    private val givenRequest = CreateGameResponse(33, "1|2", 1, 2, 3, 4)

    @Before
    fun setUp() {
        whenever(mockView.animate()).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
    }

    @Test
    fun `it should fade in when non null`() {
        LaunchBindings.resumeGame(mockView, givenRequest)
        verify(mockView).animate()
        verify(mockAnimator).alpha(1F)
        verify(mockAnimator).start()
    }

    @Test
    fun `it should fade out when null`() {
        LaunchBindings.resumeGame(mockView, null)
        verify(mockView).animate()
        verify(mockAnimator).alpha(0.5F)
        verify(mockAnimator).start()
    }

}