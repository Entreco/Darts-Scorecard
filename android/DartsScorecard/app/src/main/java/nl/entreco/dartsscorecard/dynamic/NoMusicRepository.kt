package nl.entreco.dartsscorecard.dynamic

import nl.entreco.domain.mastercaller.MusicRepository

class NoMusicRepository: MusicRepository {

    override fun play() {}

    override fun pause() {}

    override fun resume() {}

    override fun stop() {}
}