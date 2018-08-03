package nl.entreco.domain.repository

interface SignallingRepository {
    fun register(uuid: String)
}