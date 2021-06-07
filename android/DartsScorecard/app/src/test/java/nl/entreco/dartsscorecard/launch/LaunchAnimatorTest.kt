package nl.entreco.dartsscorecard.launch

import android.view.View
import android.view.ViewPropertyAnimator
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 08/02/2018.
 */
class LaunchAnimatorTest {

    private val mockView: View = mock()
    private val mockAnimator: ViewPropertyAnimator = mock()
    private lateinit var subject: LaunchAnimator.LaunchAnimatorHandler

    @Test
    fun `it should animate`() {
        givenSubject()
        whenInitializing()
        verify(mockView, atLeastOnce()).animate()
    }

    private fun givenSubject() {
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setInterpolator(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setStartDelay(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockView.animate()).thenReturn(mockAnimator)
        subject = LaunchAnimator.LaunchAnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView, mockView)
    }

    private fun whenInitializing() {
        subject.init()
    }
}
