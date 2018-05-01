package nl.entreco.data.archive

import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.common.log.Logger
import nl.entreco.domain.repository.ArchiveRepository

class ArchiveServiceRepository(db: DscDatabase, logger: Logger) : ArchiveRepository {

    private val gameDao = db.gameDao()
    private val playerDao = db.playerDao()
    private val turnDao = db.turnDao()
    private val metaDao = db.metaDao()

    override fun archive(gameId: Long): Int {
        val game = gameDao.fetchBy(gameId) ?: throw IllegalArgumentException("Unable to fetch game with id $gameId")
        if(!game.finished) throw IllegalArgumentException("Unable to archive stats for unfinished game")

        val teams = game.teams
        val turnTable = turnDao.fetchAll(gameId)
        val metaTable = metaDao.fetchAll(gameId)
        return 1
    }
}