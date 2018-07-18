package nl.entreco.dartsscorecard.profile

import android.widget.ImageView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.profile.view.ProfileBinding
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 26/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileBindingTest {

    @Mock
    private lateinit var mockImageView: ImageView

    @Test(expected = NullPointerException::class)
    fun loadProfileImage_normal() {
        ProfileBinding.loadProfileImage(mockImageView, "content://some.location")
        verify(mockImageView).setImageResource(R.drawable.ic_no_profile)
    }

    @Test
    fun loadProfileImage_team() {
        ProfileBinding.loadProfileImage(mockImageView, "team")
        verify(mockImageView).setImageResource(R.drawable.ic_team_profile)
    }

    @Test
    fun loadProfileImage_team0() {
        ProfileBinding.loadProfileImage(mockImageView, "team0")
        verify(mockImageView).setImageResource(R.drawable.ic_no_profile)
    }

    @Test
    fun loadProfileImage_team1() {
        ProfileBinding.loadProfileImage(mockImageView, "team1")
        verify(mockImageView).setImageResource(R.drawable.ic_team_profile)
    }

    @Test
    fun loadProfileImage_team2() {
        ProfileBinding.loadProfileImage(mockImageView, "team2")
        verify(mockImageView).setImageResource(R.drawable.ic_team_profile2)
    }

    @Test
    fun loadProfileImage_team3() {
        ProfileBinding.loadProfileImage(mockImageView, "team3")
        verify(mockImageView).setImageResource(R.drawable.ic_team_profile)
    }

    @Test(expected = NullPointerException::class)
    fun loadProfileImage_blank() {
        ProfileBinding.loadProfileImage(mockImageView, " ")
        verify(mockImageView, never()).setImageURI(any())
    }

    @Test(expected = NullPointerException::class)
    fun loadProfileImage_empty() {
        ProfileBinding.loadProfileImage(mockImageView, "")
        verify(mockImageView, never()).setImageURI(any())
    }

    @Test(expected = NullPointerException::class)
    fun loadProfileImage_null() {
        ProfileBinding.loadProfileImage(mockImageView, null)
        verify(mockImageView, never()).setImageURI(any())
    }

}
