package nl.entreco.domain.repository

interface RatingPrefRepository {
    fun neverAskAgain()
    fun shouldAskToRateApp() : Boolean
}