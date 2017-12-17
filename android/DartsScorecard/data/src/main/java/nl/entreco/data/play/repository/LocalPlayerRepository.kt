package nl.entreco.data.play.repository

import nl.entreco.data.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.PlayerRepository

/**
 * Created by Entreco on 16/12/2017.
 */
class LocalPlayerRepository(db: DscDatabase, private val mapper: Mapper<PlayerTable, Player>) : PlayerRepository {

    private val playerDao: PlayerDao = db.playerDao()

    override fun create(name: String, double: Int): Long {
        val player = PlayerTable()
        player.name = name
        player.fav = double.toString()
        return playerDao.create(player)
    }

    override fun fetchById(id: Long): Player? {
        val table = playerDao.fetchById(id) ?: return null
        return mapper.to(table)
    }

    override fun fetchByName(name: String): Player? {
        val table = playerDao.fetchByName(name) ?: return null
        return mapper.to(table)
    }

    override fun fetchTeams(teams: String): Array<Team> {
        return emptyArray()
    }

    override fun fetchAll(): List<Player> {
        val table = playerDao.fetchAll()
        return mapper.to(table)
    }
}