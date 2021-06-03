package nl.entreco.dartsscorecard.profile.view

import android.content.Intent
import android.net.Uri
import org.mockito.kotlin.*
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.profile.fetch.FetchProfileResponse
import nl.entreco.domain.profile.fetch.FetchProfileStatsUsecase
import nl.entreco.domain.profile.fetch.FetchProfileUsecase
import nl.entreco.domain.profile.update.UpdateProfileResponse
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

    @Mock private lateinit var mockUri: Uri
    @Mock private lateinit var mockIntent: Intent
    @Mock private lateinit var mockFetchProfile: FetchProfileUsecase
    @Mock private lateinit var mockUpdateProfile: UpdateProfileUsecase
    @Mock private lateinit var mockFetchProfileStat: FetchProfileStatsUsecase
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

    @Test
    fun `it should fetch profiles when ids provided`() {
        givenSubject()
        whenFetchingProfiles(1,2)
        thenProfilesAreFetched()
    }

    @Test
    fun `it should fetch profile stats when ids provided`() {
        givenSubject()
        whenFetchingProfiles(1,2)
        thenProfilesStatsAreFetched()
    }

    @Test
    fun `it should NOT fetch profiles when NO ids provided`() {
        givenSubject()
        whenFetchingProfiles()
        thenProfilesAreNotFetched()
    }

    @Test
    fun `it should store Profile(0) when profiles are fetched`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1,2)
        thenProfileIdIs(0) // 0 -> means TeamProfile
    }

    @Test
    fun `it should fetch Profiles once when profiles already loaded`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1,2)
        whenFetchingProfiles(4,5)
        thenProfilesAreNotFetched()
    }

    @Test
    fun `it should NOT set error msg when profiles are fetched`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1,2)
        thenErrorMessageIs(0)
    }

    @Test
    fun `it should set error msg when profiles cannot be fetched`() {
        givenSubject()
        whenFetchingProfilesFails()
        thenErrorMessageIs(R.string.err_unable_to_fetch_players)
    }

    @Test
    fun `it should update name when name updated`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingName("Remco")
        thenProfileNameIs("Remco")
    }

    @Test
    fun `it should update double when double updated`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingDouble(20)
        thenProfileDoubleIs(20)
    }

    @Test
    fun `it should update image when image updated`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingImage("some image")
        thenProfileImageIs("some image")
    }

    @Test
    fun `it should update profile when name or double updating profile succeeds`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingProfileSucceeds("Remco", 25)
        thenProfileNameIs("Remco")
        thenProfileDoubleIs(25)
    }

    @Test
    fun `it should update profile when name or double even when updating profile fails`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingProfileFails("Remco", 25)
        thenProfileNameIs("Remco")
        thenProfileDoubleIs(25)
    }

    @Test
    fun `it should show errorMessage when updating profile fails`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingProfileFails("Remco", 25)
        thenErrorMessageIs(R.string.err_unable_to_fetch_players)
    }

    @Test
    fun `it should update image when updating Image succeeds`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingImageSucceeds("some image")
        thenProfileImageIs("some image")
    }

    @Test
    fun `it should update image even when updating Image fails`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingImageFails("some image")
        thenProfileImageIs("some image")
    }

    @Test
    fun `it should show errorMessage when updating Image fails`() {
        givenSubject()
        whenFetchingProfilesSucceeds(1)
        whenUpdatingImageFails("some image")
        thenErrorMessageIs(R.string.err_unable_to_fetch_players)
    }

    private fun givenSubject() {
        subject = ProfileViewModel(mockFetchProfile, mockUpdateProfile, mockFetchProfileStat)
    }

    private fun whenFetchingProfiles(vararg ids: Long) {
        subject.fetchProfile(ids.map { it }.toLongArray())
    }

    private fun whenUpdatingName(name: String) {
        subject.showNameForProfile(name, 0)
    }

    private fun whenUpdatingDouble(double: Int) {
        subject.showNameForProfile("some name", double)
    }
    private fun whenUpdatingImage(image: String) {
        whenever(mockUri.toString()).thenReturn(image)
        whenever(mockIntent.data).thenReturn(mockUri)
        subject.showImageForProfile(mockIntent, 200F)
    }

    private fun whenFetchingProfilesSucceeds(vararg ids: Long) {
        val doneCaptor = argumentCaptor<(FetchProfileResponse)->Unit>()
        subject.fetchProfile(ids)
        verify(mockFetchProfile).exec(any(), doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(FetchProfileResponse(ids.map { Profile("$it", it, "image$it", PlayerPrefs(0)) }))
    }

    private fun whenFetchingProfilesFails() {
        val ids = listOf<Long>(1,2,3).toLongArray()
        val failCaptor = argumentCaptor<(Throwable)->Unit>()
        subject.fetchProfile(ids)
        verify(mockFetchProfile).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(RuntimeException("Unable to fetch player profiles"))
    }

    private fun whenUpdatingProfileSucceeds(name: String, double: Int) {
        val doneCaptor = argumentCaptor<(UpdateProfileResponse)->Unit>()
        subject.showNameForProfile(name, double)
        verify(mockUpdateProfile).exec(any(), doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(UpdateProfileResponse(Profile(name, 2, "", PlayerPrefs(double))))
    }

    private fun whenUpdatingProfileFails(name: String, double: Int) {
        val failCaptor = argumentCaptor<(Throwable)->Unit>()
        subject.showNameForProfile(name, double)
        verify(mockUpdateProfile).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(RuntimeException("unable to update"))
    }

    private fun whenUpdatingImageSucceeds(image: String) {
        val doneCaptor = argumentCaptor<(UpdateProfileResponse)->Unit>()
        whenever(mockUri.toString()).thenReturn(image)
        whenever(mockIntent.data).thenReturn(mockUri)
        subject.showImageForProfile(mockIntent, 200F)
        verify(mockUpdateProfile).exec(any(), doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(UpdateProfileResponse(Profile("name", 2, image, PlayerPrefs(0))))
    }

    private fun whenUpdatingImageFails(image: String) {
        val failCaptor = argumentCaptor<(Throwable)->Unit>()
        whenever(mockUri.toString()).thenReturn(image)
        whenever(mockIntent.data).thenReturn(mockUri)
        subject.showImageForProfile(mockIntent, 200F)
        verify(mockUpdateProfile).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(RuntimeException("unable to update"))
    }

    private fun thenProfilesAreFetched() {
        verify(mockFetchProfile).exec(any(), any(), any())
    }

    private fun thenProfilesStatsAreFetched() {
        verify(mockFetchProfileStat).exec(any(), any(), any())
    }

    private fun thenProfilesAreNotFetched() {
        verifyZeroInteractions(mockFetchProfile)
    }

    private fun thenErrorMessageIs(expected: Int) {
        assertEquals(expected, subject.errorMsg.get())
    }

    private fun thenProfileIdIs(expected: Long) {
        assertEquals(expected, subject.profile.get()?.id)
    }

    private fun thenProfileNameIs(expected: String) {
        assertEquals(expected, subject.profile.get()?.name?.get())
    }

    private fun thenProfileDoubleIs(expected: Int) {
        assertEquals(expected, subject.profile.get()?.fav?.get())
    }

    private fun thenProfileImageIs(expected: String) {
        assertEquals(expected, subject.profile.get()?.image?.get())
    }
}