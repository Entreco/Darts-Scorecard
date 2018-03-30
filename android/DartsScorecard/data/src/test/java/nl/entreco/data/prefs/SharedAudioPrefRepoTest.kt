package nl.entreco.data.prefs

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 17/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SharedAudioPrefRepoTest {

    @Mock private lateinit var mockEditor: SharedPreferences.Editor
    @Mock private lateinit var mockPrefs: SharedPreferences

    private lateinit var subject: SharedAudioPrefRepo

    @Test
    fun `it should get isEnabled from prefs`() {
        givenSubject()
        whenCheckingIsEnabled()
        thenPreferencesIsUsed()
    }

    @Test
    fun `it should enable mastercaller (true)`() {
        givenSubject()
        whenEnablingMastercaller(true)
        thenValueIsStoredInPrefs(true)
    }

    @Test
    fun `it should enable mastercaller (false)`() {
        givenSubject()
        whenEnablingMastercaller(false)
        thenValueIsStoredInPrefs(false)
    }

    private fun givenSubject() {
        subject = SharedAudioPrefRepo(mockPrefs)
    }

    private fun whenCheckingIsEnabled() {
        subject.isMasterCallerEnabled()
    }

    private fun whenEnablingMastercaller(enable: Boolean) {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        subject.setMasterCallerEnabled(enable)
    }

    private fun thenPreferencesIsUsed() {
        verify(mockPrefs).getBoolean("PREF_mastercaller", false)
    }

    private fun thenValueIsStoredInPrefs(expected: Boolean) {
        verify(mockPrefs).edit()
        verify(mockEditor).putBoolean("PREF_mastercaller", expected)
        verify(mockEditor).apply()
    }

}
