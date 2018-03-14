package nl.entreco.domain.play.mastercaller

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.AudioPrefRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ToggleSoundUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    private val toggleCaptor = argumentCaptor<Boolean>()

    @Mock private lateinit var mockAudioPrefs: AudioPrefRepository
    private lateinit var subject : ToggleSoundUsecase

    @Test
    fun `it should toggle sound off`() {
        givenSubject(true)
        whenToggling()
        thenSoundIs(false)
    }

    @Test
    fun `it should toggle sound on`() {
        givenSubject(false)
        whenToggling()
        thenSoundIs(true)
    }

    @Test
    fun `it should return current toggle state`() {
        givenSubject(true)
        whenIsEnabled()
        thenPrefsIsChecked()
    }

    private fun givenSubject(enabled: Boolean) {
        whenever(mockAudioPrefs.isMasterCallerEnabled()).thenReturn(enabled)
        subject = ToggleSoundUsecase(mockAudioPrefs, bg, fg)
    }

    private fun whenToggling() {
        subject.toggle()
    }

    private fun whenIsEnabled() {
        subject.isEnabled()
    }

    private fun thenSoundIs(expected: Boolean) {
        verify(mockAudioPrefs).setMasterCallerEnabled(toggleCaptor.capture())
        assertEquals(expected, toggleCaptor.lastValue)
    }

    private fun thenPrefsIsChecked() {
        verify(mockAudioPrefs).isMasterCallerEnabled()
    }

}