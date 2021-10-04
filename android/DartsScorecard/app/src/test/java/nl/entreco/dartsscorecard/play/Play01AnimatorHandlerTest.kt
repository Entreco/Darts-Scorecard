package nl.entreco.dartsscorecard.play

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.viewpager.widget.ViewPager
import nl.entreco.dartsscorecard.base.widget.MaxHeightRecyclerView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by entreco on 05/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01AnimatorHandlerTest {

    @Mock
    private lateinit var mockViewPager: ViewPager
    @Mock
    private lateinit var mockMaxHeightView: MaxHeightRecyclerView
    @Mock
    private lateinit var mockAnimator: ViewPropertyAnimator
    @Mock
    private lateinit var mockView: View
    private lateinit var subject: Play01Animator.Play01AnimatorHandler

    @Before
    fun setUp() {
        whenever(mockView.animate()).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        subject = Play01Animator.Play01AnimatorHandler(mockView, mockView, mockView, mockView,
            mockView, mockView, mockViewPager, mockMaxHeightView, mockView, mockView, mockView,
            mockView)
    }

    @Test(expected = NullPointerException::class)
    fun onSlide() {
        whenever(mockViewPager.findViewWithTag<View>(any())).thenReturn(mockView)
        subject.onSlide(100F)
        verify(mockView, atLeastOnce()).animate()
    }

    @Test
    fun onPreDraw() {
        subject.handlePreDraw()
        verify(mockMaxHeightView).requestLayout()
    }
}
