package nl.entreco.dartsscorecard.play.stats

import android.view.View
import android.view.ViewPropertyAnimator
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MatchStatAnimatorTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator

    private lateinit var subject: MatchStatAnimator.MatchStatAnimatorHandler

    @Test
    fun transform() {
        givenSubject()
        whenTransforming()
        verify(mockView, atLeastOnce()).animate()
    }

    @Test
    fun onSlide() {
        givenSubject()
        whenSliding()
        verify(mockView, atLeastOnce()).animate()
    }

    private fun givenSubject() {
        subject = MatchStatAnimator.MatchStatAnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView)
    }

    private fun whenTransforming() {
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockView.animate()).thenReturn(mockAnimator)
        subject.transform(mockView, 0F)
    }

    private fun whenSliding() {
        whenever(mockAnimator.translationY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockView.animate()).thenReturn(mockAnimator)
        subject.slide(0F)
    }

}