package nl.entreco.dartsscorecard.profile

import android.support.design.widget.AppBarLayout
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.Window
import android.widget.TextView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.databinding.ToolbarProfileBinding
import nl.entreco.dartsscorecard.profile.view.ProfileAnimator
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 26/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileAnimatorTest {

    @Mock private lateinit var mockToolbarBinding: ToolbarProfileBinding
    @Mock private lateinit var mockBinding: ActivityProfileBinding
    @Mock private lateinit var mockWindow: Window
    @Mock private lateinit var mockInflater: TransitionInflater
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockAnimator: ViewPropertyAnimator
    @Mock private lateinit var mockAppbarLayout: AppBarLayout

    private lateinit var subject: ProfileAnimator

    @Test(expected = NullPointerException::class)
    fun `it should animate Profile`() {
        subject = ProfileAnimator(mockBinding, mockInflater, mockWindow)
    }

    @Test
    fun `it should animate offset changed`() {
        whenever(mockAnimator.setDuration(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.scaleX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationX(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.withEndAction(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.translationY(any())).thenReturn(mockAnimator)
        whenever(mockAnimator.alpha(any())).thenReturn(mockAnimator)
        whenever(mockView.animate()).thenReturn(mockAnimator)
        whenever(mockTextView.animate()).thenReturn(mockAnimator)
        val handler = ProfileAnimator.ProfileAnimatorHandler(mockView, mockView, mockView, mockView, mockView, mockTextView, mockView)
        handler.onOffsetChanged(12F, 66F, mockAppbarLayout, 12)
        verify(mockView, atLeastOnce()).animate()
    }
}
