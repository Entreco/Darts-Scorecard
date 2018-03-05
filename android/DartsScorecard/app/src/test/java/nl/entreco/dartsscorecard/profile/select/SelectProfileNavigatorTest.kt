package nl.entreco.dartsscorecard.profile.select

import android.view.View
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.exceptions.misusing.NotAMockException
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 05/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SelectProfileNavigatorTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockActivity: SelectProfileActivity
    private lateinit var subject : SelectProfileNavigator

    @Test(expected = NotAMockException::class)
    fun onProfileSelected() {
        subject = SelectProfileNavigator(mockActivity)
        verify(ProfileActivity).launch(mockActivity, mockView, longArrayOf(1))
    }

}
