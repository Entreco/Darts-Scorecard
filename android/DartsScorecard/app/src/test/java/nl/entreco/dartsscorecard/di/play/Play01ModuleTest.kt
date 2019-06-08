package nl.entreco.dartsscorecard.di.play

import android.content.Context
import android.content.SharedPreferences
import android.media.SoundPool
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.data.sound.SoundMapper
import nl.entreco.domain.repository.AudioPrefRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01ModuleTest {

    @Mock private lateinit var mockSharedPrefs: SharedPreferences
    @Mock private lateinit var mockAudioPrefs: AudioPrefRepository
    @Mock private lateinit var mockMapper: SoundMapper
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockActivity: Play01Activity

    @Test
    fun `it should not be null`() {
        assertNotNull(subject())
    }

    @Test
    fun `it should provide Play01Activity`() {
        assertEquals(mockActivity, subject().provide01Activity())
    }

    @Test(expected = NullPointerException::class) // SoundPool.Builder
    fun `it should provide SoundRepository`() {
        assertNotNull(subject().provideSoundRepository(mockContext, mockMapper, mockAudioPrefs))
    }

    @Test
    fun `it should provide soundMapper`() {
        assertNotNull(subject().provideSoundMapper())
    }

    @Test
    fun `it should provide ArchiveServiceLauncher`() {
        assertNotNull(subject().provideArchiveServiceLauncher(mockContext))
    }

    private fun subject(): Play01Module {
        return Play01Module(mockActivity)
    }
}
