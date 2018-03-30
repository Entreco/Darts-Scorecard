package nl.entreco.dartsscorecard.profile.select

import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.isNull
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
    @Mock private lateinit var mockProfile: ProfileModel
    @Mock private lateinit var mockActivity: SelectProfileActivity
    private lateinit var subject: SelectProfileNavigator


    @Test(expected = NotAMockException::class)
    fun onProfileSelected() {
        givenSubject()
        subject.onProfileSelected(mockView, mockProfile)
        verify(ProfileActivity).launch(mockActivity, mockView, longArrayOf(1))
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
