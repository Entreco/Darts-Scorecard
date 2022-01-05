package nl.entreco.dartsscorecard.sounds

import android.content.Context
import android.media.SoundPool
import androidx.annotation.RawRes
import nl.entreco.domain.mastercaller.Fx00
import nl.entreco.domain.mastercaller.Fx01
import nl.entreco.domain.mastercaller.Sound
import nl.entreco.domain.repository.AudioPrefRepository
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/*************************************************************************
 *
 * ONWARD CONFIDENTIAL
 * __________________
 *
 * [2021] GTX medical
 * All Rights Reserved.
 *
 */
class LocalSoundRepositoryTest {

    private val normalPriority = 1
    private val fx0 = Fx00
    private val fx1 = Fx01

    private val mockAudioRepo: AudioPrefRepository = mock()
    private val mockSoundPool: SoundPool = mock()
    private val mockContext: Context = mock()

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
        whenPlaying(Fx01)
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
        whenever(mockAudioRepo.isMasterCallerEnabled()).doReturn(enabled)
        subject = LocalSoundRepository(mockContext, mockSoundPool, mockAudioRepo, SoundMapper())
    }

    private fun givenLoadedSounds(vararg sounds: Sound) {
        loadedSounds = sounds.toList()
    }

    private fun whenPlaying(sound: Sound) {
        val res = SoundMapper().toRaw(sound)
        whenever(mockSoundPool.load(mockContext, res, normalPriority)).doReturn(sound.hashCode())
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
        whenever(mockSoundPool.release()).doThrow(RuntimeException("Unable to release sound Pool"))
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
        Assert.assertTrue(subject.queue.contains(sound.hashCode()))
    }

    private fun thenSoundIsStored(sound: Sound) {
        Assert.assertTrue(subject.sounds.containsKey(sound))
    }

}