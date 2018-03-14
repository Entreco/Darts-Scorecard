package nl.entreco.domain.repository

import nl.entreco.domain.play.mastercaller.Sound

/**
 * Created by entreco on 14/03/2018.
 */
interface SoundRepository {
    fun play(sound: Sound)
}