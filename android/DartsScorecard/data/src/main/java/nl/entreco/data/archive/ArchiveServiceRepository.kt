package nl.entreco.data.archive

import nl.entreco.domain.repository.ArchiveRepository

class ArchiveServiceRepository : ArchiveRepository {

    override fun archive(gameId: Long): Int {
        return 1
    }

}