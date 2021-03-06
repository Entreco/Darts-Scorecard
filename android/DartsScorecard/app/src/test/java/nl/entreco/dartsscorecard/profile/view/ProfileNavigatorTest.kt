package nl.entreco.dartsscorecard.profile.view

import android.content.res.Resources
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.widget.TextView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.exceptions.misusing.NotAMockException
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileNavigatorTest {

    @Mock private lateinit var mockTextView: TextView
    @Mock private lateinit var mockResources: Resources
    @Mock private lateinit var mockPlayerProfile: PlayerProfile
    @Mock private lateinit var mockActivity: ProfileActivity
    private lateinit var subject: ProfileNavigator

    @Before
    fun setUp() {
        subject = ProfileNavigator(mockActivity)
    }

    @Test(expected = NotAMockException::class)
    fun onChooseImage() {
        subject.onChooseImage()
        verify(ProfileActivity).selectImage(mockActivity)
    }


    @Test(expected = NotAMockException::class)
    fun onEditProfile() {
        whenever(mockPlayerProfile.name).thenReturn(ObservableField("Remco"))
        whenever(mockPlayerProfile.fav).thenReturn(ObservableInt(1))
        whenever(mockTextView.transitionName).thenReturn("transition")
        whenever(mockActivity.findViewById<TextView>(any())).thenReturn(mockTextView)
        whenever(mockResources.getStringArray(R.array.fav_doubles)).thenReturn(arrayOf("1", "2"))
        whenever(mockActivity.resources).thenReturn(mockResources)
        subject.onEditProfile(mockPlayerProfile)
        verify(ProfileActivity).selectName(eq(mockActivity), any(), any())
    }
}
