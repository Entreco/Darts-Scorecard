package nl.entreco.dartsscorecard.profile

import android.transition.TransitionInflater
import android.view.Window
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
    private lateinit var subject: ProfileAnimator

    @Test(expected = KotlinNullPointerException::class)
    fun `it should animate Profile`() {
        subject = ProfileAnimator(mockBinding, mockInflater, mockWindow)
    }
}
