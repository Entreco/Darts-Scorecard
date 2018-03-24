package nl.entreco.dartsscorecard.play

import android.view.View
import android.view.ViewPropertyAnimator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
        subject = Play01Animator.Play01AnimatorHandler(view, view, view, view, view)
    }

    @Test
    fun onSlide() {
        subject.onSlide(100F)
        verify(view, atLeastOnce()).animate()
    }
}
