package nl.entreco.data.setup.repository

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.prefs.SharedSetupPrefRepo
import nl.entreco.domain.setup.settings.FetchSettingsResponse
import nl.entreco.domain.setup.settings.StoreSettingsRequest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SharedPreferenceRepoTest {

    @Mock private lateinit var mockSharedPrefs: SharedPreferences
    @Mock private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var subject: SharedSetupPrefRepo
    private lateinit var givenRequest: StoreSettingsRequest
    private lateinit var actualResponse: FetchSettingsResponse

    @Test
    fun fetchPreferredSetup() {
        givenSubject()
        givenNoPreviousSetupStored()
        whenFetchingPreferredSetup()
        thenDefaultResponseIsReturned()
    }

    @Test
    fun storePreferredSetup() {
        givenSubject()
        whenStoringPreferredSetup(StoreSettingsRequest(1, 2, 3, 4, 5))
        thenCorrectValuesAreStored()
    }

    private fun givenSubject() {
        subject = SharedSetupPrefRepo(mockSharedPrefs)
    }

    private fun givenNoPreviousSetupStored() {
        // default mocking
        whenever(mockSharedPrefs.getInt(SharedSetupPrefRepo.PREF_SETS, FetchSettingsResponse.def_sets)).thenReturn(FetchSettingsResponse.def_sets)
        whenever(mockSharedPrefs.getInt(SharedSetupPrefRepo.PREF_LEGS, FetchSettingsResponse.def_legs)).thenReturn(FetchSettingsResponse.def_legs)
        whenever(mockSharedPrefs.getInt(SharedSetupPrefRepo.PREF_MIN, FetchSettingsResponse.def_min)).thenReturn(FetchSettingsResponse.def_min)
        whenever(mockSharedPrefs.getInt(SharedSetupPrefRepo.PREF_MAX, FetchSettingsResponse.def_max)).thenReturn(FetchSettingsResponse.def_max)
        whenever(mockSharedPrefs.getInt(SharedSetupPrefRepo.PREF_SCORE, FetchSettingsResponse.def_start)).thenReturn(FetchSettingsResponse.def_start)
    }

    private fun whenFetchingPreferredSetup() {
        actualResponse = subject.fetchPreferredSetup()
    }

    private fun whenStoringPreferredSetup(request: StoreSettingsRequest) {
        whenever(mockSharedPrefs.edit()).thenReturn(mockEditor)
        givenRequest = request
        subject.storePreferredSetup(request)
    }

    private fun thenDefaultResponseIsReturned() {
        assertEquals(FetchSettingsResponse(), actualResponse)
    }

    private fun thenCorrectValuesAreStored() {
        verify(mockSharedPrefs).edit()
        verify(mockEditor).putInt(SharedSetupPrefRepo.PREF_SETS, givenRequest.sets)
        verify(mockEditor).putInt(SharedSetupPrefRepo.PREF_LEGS, givenRequest.legs)
        verify(mockEditor).putInt(SharedSetupPrefRepo.PREF_MIN, givenRequest.min)
        verify(mockEditor).putInt(SharedSetupPrefRepo.PREF_MAX, givenRequest.max)
        verify(mockEditor).putInt(SharedSetupPrefRepo.PREF_SCORE, givenRequest.score)
    }

}