package nl.entreco.dartsscorecard.beta

import android.support.design.widget.AppBarLayout
import android.view.View
import android.view.ViewPropertyAnimator
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 05/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaAnimatorHandlerTest {

    @Mock private lateinit var mockAnimator: ViewPropertyAnimator
    @Mock private lateinit var mockAppBar: AppBarLayout
    @Mock private lateinit var view: View
    private lateinit var subject : BetaAnimator.BetaAnimatorHandler

    @Test
    fun onOffsetChanged() {
        mockOffset()
        subject.onOffsetChanged(mockAppBar, 12)
        verify(view, atLeastOnce()).animate()
    }

    @Test
    fun onSlide() {
        mockSlide()
        subject.onSlide(12F)
        verify(view, atLeastOnce()).animate()
    }

    @Test
    fun `it should enable when collapsed`() {
        mockState()
        subject.onStateChanged(4)
        verify(mockAppBar).isEnabled = true
    }

    @Test
    fun `it should NOT enable when NOT collapsed`() {
        mockState()
        subject.onStateChanged(3)
        verify(mockAppBar, never()).isEnabled = true
    }

    private fun mockOffset() {
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(view.animate()).thenReturn(mockAnimator)
        subject = BetaAnimator.BetaAnimatorHandler(mockAppBar, view, view, view)
    }


    private fun mockSlide() {
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.rotationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(view.animate()).thenReturn(mockAnimator)
        subject = BetaAnimator.BetaAnimatorHandler(mockAppBar, view, view, view)
    }

    private fun mockState() {
        subject = BetaAnimator.BetaAnimatorHandler(mockAppBar, view, view, view)
    }

}
