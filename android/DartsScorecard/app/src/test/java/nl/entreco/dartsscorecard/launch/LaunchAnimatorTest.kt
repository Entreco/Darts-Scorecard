package nl.entreco.dartsscorecard.launch

import nl.entreco.dartsscorecard.databinding.ActivityLaunchBinding
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 08/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LaunchAnimatorTest {

    @Mock private lateinit var mockBinding: ActivityLaunchBinding
    private lateinit var subject: LaunchAnimator

    @Test(expected = NullPointerException::class)
    fun `it should animate`() {
        givenSubject()
    }

    private fun givenSubject() {
        subject = LaunchAnimator(mockBinding)
    }
}