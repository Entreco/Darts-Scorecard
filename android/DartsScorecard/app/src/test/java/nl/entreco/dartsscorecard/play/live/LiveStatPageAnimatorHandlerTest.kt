package nl.entreco.dartsscorecard.play.live

import android.view.View
import android.view.ViewGroup
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
 * Created by entreco on 27/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LiveStatPageAnimatorHandlerTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockViewGroup: ViewGroup
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator

    private lateinit var subject: LiveStatPageAnimator.MatchStatPageAnimatorHandler

    @Test
    fun transform() {
        givenSubject()
        whenTransforming()
        verify(mockView, atLeastOnce()).animate()
    }

    private fun givenSubject() {
        subject = LiveStatPageAnimator.MatchStatPageAnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockViewGroup, 100F)
    }

    private fun whenTransforming() {
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockView.animate()).thenReturn(mockAnimator)
        subject.transform(mockView, 0F)
    }

}