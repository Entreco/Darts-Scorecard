package nl.entreco.dartsscorecard.profile

import android.support.design.widget.AppBarLayout
import android.transition.TransitionInflater
import android.view.View
import android.view.Window
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
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

    @Mock private lateinit var mockBinding: ActivityProfileBinding
    @Mock private lateinit var mockWindow: Window
    @Mock private lateinit var mockInflater: TransitionInflater
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockAppbarLayout: AppBarLayout

    private lateinit var subject: ProfileAnimator

    @Test(expected = IllegalStateException::class)
    fun `it should animate Profile`() {
        subject = ProfileAnimator(mockBinding, mockInflater, mockWindow)
    }

    @Test
    fun `it should animate offset changed`() {
        val handler = ProfileAnimator.ProfileAnimatorHandler(mockView, mockView)
        handler.onOffsetChanged( mockAppbarLayout, 12)
        verify(mockView, atLeastOnce()).startAnimation(any())
    }
}
