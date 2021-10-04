package nl.entreco.dartsscorecard.profile

import android.transition.TransitionInflater
import android.view.View
import android.view.Window
import com.google.android.material.appbar.AppBarLayout
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.profile.view.ProfileAnimator
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Created by entreco on 26/02/2018.
 */
class ProfileAnimatorTest {

    private val mockBinding: ActivityProfileBinding = mock()
    private val mockWindow: Window = mock()
    private val mockInflater: TransitionInflater = mock()
    private val mockView: View = mock()
    private val mockAppbarLayout: AppBarLayout = mock()

    private lateinit var subject: ProfileAnimator

    @Test(expected = NullPointerException::class)
    fun `it should animate Profile`() {
        subject = ProfileAnimator(mockBinding, mockInflater, mockWindow)
    }

    @Test
    fun `it should animate offset changed`() {
        val handler = ProfileAnimator.ProfileAnimatorHandler(mockView, mockView)
        handler.onOffsetChanged(mockAppbarLayout, 12)
        verify(mockView, atLeastOnce()).startAnimation(any())
    }
}
