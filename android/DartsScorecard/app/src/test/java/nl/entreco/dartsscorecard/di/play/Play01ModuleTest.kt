package nl.entreco.dartsscorecard.di.play

import android.content.Context
import android.media.SoundPool
import nl.entreco.data.sound.SoundMapper
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

    @Mock private lateinit var mockMapper: SoundMapper
    @Mock private lateinit var mockSoundPool: SoundPool
    @Mock private lateinit var mockContext: Context

    @Test
    fun `it should not be null`() {
        assertNotNull(Play01Module())
    }

    @Test(expected = NullPointerException::class)
    fun `it should provide AlertDialogBuilder`() {
        Play01Module().provideAlertDialogBuilder(mockContext)
    }

    @Test
    fun `it should provide SoundRepository`() {
        assertNotNull(Play01Module().provideSoundRepository(mockContext, mockSoundPool, mockMapper))
    }

    @Test
    fun `it should provide soundMapper`() {
        assertNotNull(Play01Module().provideSoundMapper())
    }

    @Test(expected = NullPointerException::class) // SoundPool.Builder() cannot be mocked
    fun `it should provide soundPool`() {
        assertNotNull(Play01Module().provideSoundPool())
    }
}
