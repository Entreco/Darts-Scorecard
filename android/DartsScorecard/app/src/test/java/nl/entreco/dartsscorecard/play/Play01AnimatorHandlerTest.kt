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

    @Mock private lateinit var viewPager: ViewPager
    @Mock private lateinit var maxHeightView: MaxHeightRecyclerView
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator
    @Mock private lateinit var view: View
    private lateinit var subject: Play01Animator.Play01AnimatorHandler

    @Before
    fun setUp() {
        whenever(view.animate()).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        subject = Play01Animator.Play01AnimatorHandler(view, view, view, view, view, view, viewPager, maxHeightView, view, view, view, view)
    }

    @Test
    fun onSlide() {
        subject.onSlide(100F)
        verify(view, atLeastOnce()).animate()
    }

    @Test
    fun setPage() {
        subject.setPage(0)
        verify(viewPager).getChildAt(0)

    }

    @Test
    fun onPreDraw() {
        subject.onPreDraw()
        verify(maxHeightView).requestLayout()
    }
}
