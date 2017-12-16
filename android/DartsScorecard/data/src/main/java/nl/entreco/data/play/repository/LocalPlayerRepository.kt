package nl.entreco.data.play.repository

import nl.entreco.data.DscDatabase
import nl.entreco.data.db.player.PlayerDao
import nl.entreco.data.db.player.PlayerMapper
import nl.entreco.data.db.player.PlayerTable
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.repository.PlayerRepository
import java.util.*

/**
 * Created by Entreco on 16/12/2017.
 */
class LocalPlayerRepository(db: DscDatabase, private val mapper: PlayerMapper) : PlayerRepository {

    private val playerDao: PlayerDao = db.playerDao()

    override fun create(name: String, double: Int): Long {
        val player = PlayerTable()
        player.uid = UUID.randomUUID().toString()
        player.name = name
        player.fav = double.toString()
        return playerDao.create(player)
    }

    override fun fetchByUid(uid: String): Player? {
        val table = playerDao.fetchByUid(uid) ?: return null
        return mapper.to(table)
    }

    override fun fetchByName(name: String): Player? {
        val table = playerDao.fetchByName(name) ?: return null
        return mapper.to(table)
    }

    override fun fetchAll(): List<Player> {
        val table = playerDao.fetchAll()
        return mapper.to(table)
    }
}