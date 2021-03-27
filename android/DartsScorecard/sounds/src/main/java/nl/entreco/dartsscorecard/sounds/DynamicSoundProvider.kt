package nl.entreco.dartsscorecard.sounds

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.annotation.Keep
import nl.entreco.dartsscorecard.di.play.Play01Scope
import nl.entreco.dartsscorecard.dynamic.SoundModuleProvider
import nl.entreco.domain.mastercaller.MusicRepository
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.domain.repository.AudioPrefRepository

/**
 * Accessed dynamically through reflection,
 * if the Dynamic Sound module is installed :nerd:
 */
@Keep
object DynamicSoundProvider : SoundModuleProvider {

    override fun provideSoundRepository(context: Context, @Play01Scope prefs: AudioPrefRepository): SoundRepository {
        val mapper = SoundMapper()
        val soundPool = SoundPool.Builder().setMaxStreams(2).build()
        return LocalSoundRepository(context, soundPool, prefs, mapper)
    }

    override fun provideMusicRepository(context: Context): MusicRepository? {
        val mediaPlayer = try {
            MediaPlayer.create(context, nl.entreco.dartsscorecard.sounds.R.raw.pdc_tune)
        } catch (oops: Resources.NotFoundException) {
            null
        } ?: return null

        mediaPlayer.isLooping = true
        return LocalMusicRepository(mediaPlayer)
    }
}