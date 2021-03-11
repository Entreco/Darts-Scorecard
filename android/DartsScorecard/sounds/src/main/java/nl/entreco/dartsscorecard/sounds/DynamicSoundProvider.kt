package nl.entreco.dartsscorecard.sounds

import android.content.ContentResolver
import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
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

    override fun provideMusicRepository(context: Context): MusicRepository {
//        val tuneResourceId = R.raw.pdc_tune
//        val uri = Uri.Builder()
//                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//                .authority(context.packageName)
//                .appendPath(context.resources.getResourceTypeName(tuneResourceId))
//                .appendPath(String.format("%s:%s",
//                        context.resources.getResourcePackageName(tuneResourceId),
//                        context.resources.getResourceEntryName(tuneResourceId)))
//                .build()
        val mediaPlayer = MediaPlayer.create(context, R.raw.pdc_tune)
        mediaPlayer.isLooping = true
        return LocalMusicRepository(mediaPlayer)
    }
}