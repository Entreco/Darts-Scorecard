package nl.entreco.dartsscorecard.profile

import android.net.Uri
import android.widget.ImageView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
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

    @Mock private lateinit var mockImageView : ImageView

    @Test
    fun loadProfileImage_normal() {
        ProfileBinding.loadProfileImage(mockImageView, "content://some.location")
        verify(mockImageView, atLeastOnce()).setImageURI(Uri.parse("content://some.location"))
    }

    @Test
    fun loadProfileImage_blank() {
        ProfileBinding.loadProfileImage(mockImageView, " ")
        verify(mockImageView, never()).setImageURI(any())
    }

    @Test
    fun loadProfileImage_null() {
        ProfileBinding.loadProfileImage(mockImageView, null)
        verify(mockImageView, never()).setImageURI(any())
    }

}
