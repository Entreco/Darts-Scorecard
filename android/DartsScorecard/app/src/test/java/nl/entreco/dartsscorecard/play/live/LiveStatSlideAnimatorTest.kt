package nl.entreco.dartsscorecard.play.live

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
class LiveStatSlideAnimatorTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator

    private lateinit var subject: LiveStatSlideAnimator.MatchStatSlideAnimatorHandler

    @Test
    fun onSlide() {
        givenSubject()
        whenSliding()
        verify(mockView, atLeastOnce()).animate()
    }

    private fun givenSubject() {
        subject = LiveStatSlideAnimator.MatchStatSlideAnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView)
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