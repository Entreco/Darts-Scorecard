package nl.entreco.domain.repository

interface SignallingRepository {
    fun register(done: (String)->Unit)
    fun findReceivers(id: String, done: (String) -> Unit)
}