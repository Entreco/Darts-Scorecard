package nl.entreco.dartsscorecard.play

import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewPropertyAnimator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.base.widget.MaxHeightRecyclerView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 05/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01AnimatorHandlerTest {

    @Mock private lateinit var mockViewPager: ViewPager
    @Mock private lateinit var mockMaxHeightView: MaxHeightRecyclerView
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator
    @Mock private lateinit var mockView: View
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
        subject = Play01Animator.Play01AnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockView, mockViewPager, mockMaxHeightView, mockView, mockView, mockView, mockView)
    }

    @Test
    fun onSlide() {
        whenever(mockViewPager.getChildAt(any())).thenReturn(mockView)
        subject.onSlide(100F)
        verify(mockView, atLeastOnce()).animate()
    }

    @Test
    fun setPage() {
        whenever(mockViewPager.getChildAt(0)).thenReturn(mockView)
        subject.storePositionForAnimator(0)
        verify(mockViewPager).getChildAt(0)

    }

    @Test
    fun onPreDraw() {
        subject.onPreDraw()
        verify(mockMaxHeightView).requestLayout()
    }
}
