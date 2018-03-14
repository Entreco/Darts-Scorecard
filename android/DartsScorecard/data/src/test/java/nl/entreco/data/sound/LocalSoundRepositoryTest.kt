package nl.entreco.data.sound

import android.content.Context
import android.media.SoundPool
import android.support.annotation.RawRes
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.R
import nl.entreco.domain.play.mastercaller.Fx00
import nl.entreco.domain.play.mastercaller.Fx01
import nl.entreco.domain.play.mastercaller.Sound
import nl.entreco.domain.repository.AudioPrefRepository
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalSoundRepositoryTest {

    private val normalPriority = 1
    private val fx0 = Fx00()
    private val fx1 = Fx01()

    @Mock private lateinit var mockAudioRepo: AudioPrefRepository
    @Mock private lateinit var mockSoundPool: SoundPool
    @Mock private lateinit var mockContext: Context
    private lateinit var subject: LocalSoundRepository
    private var loadedSounds = emptyList<Sound>()

    @Test
    fun `it should release pool when releasing`() {
        givenSubject()
        whenReleasing()
        thenPoolIsReleased()
    }

    @Test
    fun `it should not crash when releasing pool throws`() {
        givenSubject()
        whenReleasingThrows()
    }

    @Test
    fun `it should load correct sound`() {
        givenSubject()
        whenPlaying(Fx01())
        thenSoundIsLoaded(R.raw.dsc_pro1)
    }

    @Test
    fun `it should queue last sound`() {
        givenSubject()
        whenPlaying(fx1)
        thenSoundIsQueued(fx1)
    }

    @Test
    fun `it should store sound for when its done loading`() {
        givenSubject()
        whenPlaying(fx1)
        thenSoundIsStored(fx1)
    }

    @Test
    fun `it should play sound after it was loaded`() {
        givenLoadedSounds(fx0)
        givenSubject()
        whenPlaying(fx0)
        whenDoneLoading(fx0)
        thenSoundIsPlayed(fx0)
    }

    @Test
    fun `it should NOT play sound when it has not been loaded`() {
        givenLoadedSounds(fx0)
        givenSubject()
        whenPlaying(fx0)
        thenSoundIsNotPlayed(fx0)
    }

    @Test
    fun `it should not play sound when audio disabled`() {
        givenSubject(false)
        whenPlaying(fx0)
        whenDoneLoading(fx0)
        thenSoundIsNotPlayed(fx0)
    }

    private fun givenSubject(enabled: Boolean = true) {
        whenever(mockAudioRepo.isMasterCallerEnabled()).thenReturn(enabled)
        subject = LocalSoundRepository(mockContext, mockSoundPool, mockAudioRepo, SoundMapper())
    }

    private fun givenLoadedSounds(vararg sounds: Sound) {
        loadedSounds = sounds.toList()
    }

    private fun whenPlaying(sound: Sound) {
        val res = SoundMapper().toRaw(sound)
        whenever(mockSoundPool.load(mockContext, res, normalPriority)).thenReturn(sound.hashCode())
        subject.play(sound)
    }

    private fun whenDoneLoading(sound: Sound) {
        val loaded = loadedSounds.contains(sound)
        if (loaded) {
            subject.storeSound(sound.hashCode())
        }
    }

    private fun whenReleasing() {
        subject.release()
    }

    private fun whenReleasingThrows() {
        whenever(mockSoundPool.release()).thenThrow(RuntimeException("Unable to release sound Pool"))
        subject.release()
    }

    private fun thenSoundIsLoaded(@RawRes resId: Int) {
        verify(mockSoundPool).load(mockContext, resId, normalPriority)
    }

    private fun thenPoolIsReleased() {
        verify(mockSoundPool).release()
    }

    private fun thenSoundIsPlayed(sound: Sound) {
        verify(mockSoundPool).play(sound.hashCode(), 1.0F, 1.0F, normalPriority, 0, 1.0f)
    }

    private fun thenSoundIsNotPlayed(sound: Sound) {
        verify(mockSoundPool, never()).play(sound.hashCode(), 1.0F, 1.0F, normalPriority, 0, 1.0f)
    }

    private fun thenSoundIsQueued(sound: Sound) {
        assertTrue(subject.queue.contains(sound.hashCode()))
    }

    private fun thenSoundIsStored(sound: Sound) {
        assertTrue(subject.sounds.containsKey(sound))
    }

}