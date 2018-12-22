package nl.entreco.domain.repository

interface MusicRepository {
    fun play()
    fun pause()
    fun resume()
    fun stop()
}