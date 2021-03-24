package nl.entreco.dartsscorecard.dynamic

import nl.entreco.domain.mastercaller.Sound
import nl.entreco.domain.mastercaller.SoundRepository

class NoSoundRepository : SoundRepository {

    override fun play(sound: Sound) {}

    override fun release() {}
}