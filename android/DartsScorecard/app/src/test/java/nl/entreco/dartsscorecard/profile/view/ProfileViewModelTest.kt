package nl.entreco.dartsscorecard.profile.view

import nl.entreco.domain.profile.fetch.FetchProfileUsecase
import nl.entreco.domain.profile.update.UpdateProfileUsecase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 20/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest{

    @Mock private lateinit var mockFetchProfile: FetchProfileUsecase
    @Mock private lateinit var mockUpdateProfile: UpdateProfileUsecase
    private lateinit var subject: ProfileViewModel

    @Before
    fun setUp() {
        givenSubject()
    }

    @Test
    fun `it should have correct initial values`() {
        assertNull(subject.profile.get())
        assertEquals(0, subject.errorMsg.get())
    }

    private fun givenSubject() {
        subject = ProfileViewModel(mockFetchProfile, mockUpdateProfile)
    }
}