package nl.entreco.dartsscorecard.dynamic

import android.content.Context
import nl.entreco.dartsscorecard.di.play.Play01Scope
import nl.entreco.domain.mastercaller.MusicRepository
import nl.entreco.domain.mastercaller.SoundRepository
import nl.entreco.domain.repository.AudioPrefRepository

/*************************************************************************
 *
 * ONWARD CONFIDENTIAL
 * __________________
 *
 *  [2021] ONWARD
 *  All Rights Reserved.
 *
 */
interface SoundModuleProvider {
    fun provideSoundRepository(context: Context, @Play01Scope prefs: AudioPrefRepository): SoundRepository
    fun provideMusicRepository(context: Context): MusicRepository
}