package nl.entreco.dartsscorecard.dynamic

import android.content.Context
import nl.entreco.dartsscorecard.di.play.Play01Scope
import nl.entreco.domain.mastercaller.MusicRepository
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.domain.repository.AudioPrefRepository

interface SoundModuleProvider {
    fun provideSoundRepository(context: Context, @Play01Scope prefs: AudioPrefRepository): SoundRepository
    fun provideMusicRepository(context: Context): MusicRepository
}