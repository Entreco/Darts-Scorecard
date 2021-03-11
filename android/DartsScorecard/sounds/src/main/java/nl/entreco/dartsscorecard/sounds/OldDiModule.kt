package nl.entreco.dartsscorecard.sounds

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.play.Play01Scope
import nl.entreco.domain.mastercaller.MusicRepository
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.domain.repository.AudioPrefRepository

@Module
object OldDiModule {

    @Provides
    @Play01Scope
    fun provideSoundMapper(): SoundMapper {
        return SoundMapper()
    }

    @Provides
    @Play01Scope
    fun provideSoundRepository(context: Context, @Play01Scope mapper: SoundMapper, @Play01Scope prefs: AudioPrefRepository): SoundRepository {
        val soundPool = SoundPool.Builder().setMaxStreams(2).build()
        return LocalSoundRepository(context, soundPool, prefs, mapper)
    }

    @Provides
    @Play01Scope
    fun provideMusicRepository(context: Context): MusicRepository {
        val mediaPlayer = MediaPlayer.create(context, R.raw.pdc_tune)
        mediaPlayer.isLooping = true
        return LocalMusicRepository(mediaPlayer)
    }
}