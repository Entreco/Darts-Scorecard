package nl.entreco.dartsscorecard.profile.select

import android.view.View
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.databinding.ProfileViewBinding
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 12/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileViewTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockProfile: Profile
    @Mock private lateinit var mockNavigator: SelectProfileNavigator
    @Mock private lateinit var mockBinding: ProfileViewBinding
    private lateinit var subject : ProfileView

    private val profileCaptor = argumentCaptor<ProfileModel>()

    @Test
    fun `it should set profile when binding`() {
        givenSubject()
        whenBinding()
        thenProfileIsBound()
    }

    @Test
    fun `it should set navigator when binding`() {
        givenSubject()
        whenBinding()
        thenNavigatorIsBound()
    }

    @Test
    fun `it should execute pending bindings when binding`() {
        givenSubject()
        whenBinding()
        thenExecutePendingBindingsIsCalled()
    }

    private fun givenSubject() {
        whenever(mockBinding.root).thenReturn(mockView)
        subject = ProfileView(mockBinding)
    }

    private fun whenBinding() {
        whenever(mockProfile.id).thenReturn(0)
        whenever(mockProfile.prefs).thenReturn(PlayerPrefs(16))
        whenever(mockProfile.name).thenReturn("name")
        whenever(mockProfile.image).thenReturn("image")
        subject.bind(mockProfile, mockNavigator)
    }

    private fun thenProfileIsBound() {
        verify(mockBinding).profile = profileCaptor.capture()
        assertEquals(mockProfile.id, profileCaptor.lastValue.id)
        assertEquals(mockProfile.name, profileCaptor.lastValue.name.get())
        assertEquals(mockProfile.image, profileCaptor.lastValue.image.get())
    }

    private fun thenNavigatorIsBound() {
        verify(mockBinding).navigator = mockNavigator
    }

    private fun thenExecutePendingBindingsIsCalled() {
        verify(mockBinding).executePendingBindings()
    }
}