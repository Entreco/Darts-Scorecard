package nl.entreco.data.db.profile

import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.players.PlayerSeperator
import nl.entreco.domain.model.players.TeamSeperator
import nl.entreco.domain.repository.ArchiveRepository

class ArchiveServiceRepository(db: DscDatabase, private val mapper: ArchiveStatMapper) : ArchiveRepository {

    private val gameDao = db.gameDao()
    private val turnDao = db.turnDao()
    private val metaDao = db.metaDao()
    private val profileDao = db.profileDao()

    override fun archive(gameId: Long): Int {
        val game = gameDao.fetchBy(gameId) ?: throw IllegalArgumentException("Unable to fetch game with id $gameId")
        if (!game.finished) throw IllegalArgumentException("Unable to archive stats for unfinished game")

        val turnTable = turnDao.fetchAll(gameId)
        val metaTable = metaDao.fetchAll(gameId)
        val players = game.teams
                .split(TeamSeperator)
                .map { it.split(PlayerSeperator) }
                .flatMap { it }

        players.forEach { player ->
            val profileTable = mapper.to(gameId, player.toLong(), game.winningTeam, turnTable, metaTable)
            profileDao.create(profileTable)
        }

        return 1
    }
}