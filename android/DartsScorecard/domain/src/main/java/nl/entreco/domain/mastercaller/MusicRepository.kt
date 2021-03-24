package nl.entreco.domain.mastercaller

interface MusicRepository {
    fun play()
    fun pause()
    fun resume()
    fun stop()
}