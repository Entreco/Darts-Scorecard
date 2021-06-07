package nl.entreco.dartsscorecard.profile.select

import android.view.View
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.isNull
import org.mockito.kotlin.verify
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
    @Mock private lateinit var mockProfile: ProfileModel
    @Mock private lateinit var mockActivity: SelectProfileActivity
    private lateinit var subject: SelectProfileNavigator


    @Test(expected = NotAMockException::class)
    fun onProfileSelected() {
        givenSubject()
        subject.onProfileSelected(mockView, mockProfile)
        verify(ProfileActivity).launch(mockActivity, longArrayOf(1), mockView)
    }

    @Test
    fun onProfileCreate() {
        givenSubject()
        subject.onCreateProfile(mockView)
        verify(mockActivity).startActivityForResult(any(), eq(SelectProfileActivity.REQUEST_CODE_CREATE), isNull())
    }

    private fun givenSubject() {
        subject = SelectProfileNavigator(mockActivity)
    }
}
