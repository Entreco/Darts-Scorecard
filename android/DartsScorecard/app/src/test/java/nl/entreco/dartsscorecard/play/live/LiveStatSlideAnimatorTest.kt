package nl.entreco.dartsscorecard.play.live

import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 25/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LiveStatSlideAnimatorTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockViewGroup: ViewGroup
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator

    private lateinit var subject: LiveStatSlideAnimator.MatchStatSlideAnimatorHandler

    @Test
    fun onSlide() {
        givenSubject()
        whenSliding()
        verify(mockView, atLeastOnce()).animate()
    }

    private fun givenSubject() {
        subject = LiveStatSlideAnimator.MatchStatSlideAnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockViewGroup)
    }

    private fun whenSliding() {
        whenever(mockAnimator.translationY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockViewGroup.animate()).thenReturn(mockAnimator)
        whenever(mockView.animate()).thenReturn(mockAnimator)
        subject.slide(0F)
    }

}