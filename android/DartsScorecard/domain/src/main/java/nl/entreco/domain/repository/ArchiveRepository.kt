package nl.entreco.domain.repository


interface ArchiveRepository {
    fun archive(gameId: Long) : Int
}