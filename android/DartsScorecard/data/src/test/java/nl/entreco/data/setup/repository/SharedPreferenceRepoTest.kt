package nl.entreco.data.setup.repository

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.setup.usecase.FetchSettingsResponse
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
    private lateinit var subject: SharedPreferenceRepo
    private lateinit var actualResponse: FetchSettingsResponse

    @Test
    fun fetchPreferredSetup() {
        givenSubject()
        givenNoPreviousSetupStored()
        whenFetchingPreferredSetup()
        thenDefaultResponseIsReturned()
    }

    private fun givenSubject() {
        subject = SharedPreferenceRepo(mockSharedPrefs)
    }

    private fun givenNoPreviousSetupStored() {
        // default mocking
        whenever(mockSharedPrefs.getInt(SharedPreferenceRepo.PREF_SETS, FetchSettingsResponse.def_sets)).thenReturn(FetchSettingsResponse.def_sets)
        whenever(mockSharedPrefs.getInt(SharedPreferenceRepo.PREF_LEGS, FetchSettingsResponse.def_legs)).thenReturn(FetchSettingsResponse.def_legs)
        whenever(mockSharedPrefs.getInt(SharedPreferenceRepo.PREF_MIN, FetchSettingsResponse.def_min)).thenReturn(FetchSettingsResponse.def_min)
        whenever(mockSharedPrefs.getInt(SharedPreferenceRepo.PREF_MAX, FetchSettingsResponse.def_max)).thenReturn(FetchSettingsResponse.def_max)
        whenever(mockSharedPrefs.getInt(SharedPreferenceRepo.PREF_SCORE, FetchSettingsResponse.def_start)).thenReturn(FetchSettingsResponse.def_start)
    }

    private fun whenFetchingPreferredSetup() {
        actualResponse = subject.fetchPreferredSetup()
    }

    private fun thenDefaultResponseIsReturned() {
        assertEquals(FetchSettingsResponse(), actualResponse)
    }

}