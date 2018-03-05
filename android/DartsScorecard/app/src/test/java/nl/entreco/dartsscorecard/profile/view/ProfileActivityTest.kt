package nl.entreco.dartsscorecard.profile.view

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.players.Team
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 05/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileActivityTest {


    @Mock private lateinit var mockTeam: Team
    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockActivity: Activity
    val subject = spy(ProfileActivity())

    @Test
    fun `can be created`() {
        Assert.assertNotNull(subject)
    }

    @Test(expected = NullPointerException::class)
    fun launch() {
        ProfileActivity.launch(mockActivity, mockView, mockTeam)
        verify(mockActivity).startActivityForResult(any(), eq(1222))
    }

    @Test
    fun selectImage() {
        assertNotNull(ProfileActivity.selectImage(mockActivity))
    }

    @Test
    fun selectName() {
        whenever(mockTextView.transitionName).thenReturn("transition")
        whenever(mockActivity.findViewById<TextView>(any())).thenReturn(mockTextView)
        assertNotNull(ProfileActivity.selectName(mockActivity, "some name", "some double"))
    }

}
