package nl.entreco.dartsscorecard.profile.view

import android.app.Activity
import android.content.res.Resources
import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.profile.select.SelectProfileActivity
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
    @Mock private lateinit var mockResources: Resources
    @Mock private lateinit var mockActivity: Activity
    val subject = spy(ProfileActivity())

    @Test
    fun `can be created`() {
        Assert.assertNotNull(subject)
    }

    @Test
    fun launch() {
        ProfileActivity.launch(mockActivity, mockView, longArrayOf(1, 2))
        verify(mockActivity).startActivityForResult(any(), eq(SelectProfileActivity.REQUEST_CODE_VIEW), isNull())
    }

    @Test
    fun selectImage() {
        assertNotNull(ProfileActivity.selectImage(mockActivity))
    }

    @Test
    fun selectName() {
        whenever(mockResources.getStringArray(R.array.fav_doubles)).thenReturn(arrayOf("1", "2"))
        whenever(mockActivity.resources).thenReturn(mockResources)
        whenever(mockTextView.transitionName).thenReturn("transition")
        whenever(mockActivity.findViewById<TextView>(any())).thenReturn(mockTextView)
        assertNotNull(ProfileActivity.selectName(mockActivity, "some name", 1))
    }

}
