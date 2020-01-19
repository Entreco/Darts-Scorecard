package nl.entreco.domain.repository

interface BotPrefRepository {
    fun setBotSpeed(speed: Int)
    fun getBotSpeed() : Int
}