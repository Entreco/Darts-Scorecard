package nl.entreco.data.sound

import android.media.MediaPlayer
import nl.entreco.domain.repository.MusicRepository

class LocalMusicRepository(private val mediaPlayer: MediaPlayer): MusicRepository {

    override fun play() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun resume() {
        mediaPlayer.start()
    }

    override fun stop() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}