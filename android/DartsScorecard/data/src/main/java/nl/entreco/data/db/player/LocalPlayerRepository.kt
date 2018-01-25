package nl.entreco.data.db.player

import android.support.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository

/**
 * Created by Entreco on 16/12/2017.
 */
class LocalPlayerRepository(db: DscDatabase, private val mapper: Mapper<PlayerTable, Player>) : PlayerRepository {

    private val playerDao: PlayerDao = db.playerDao()

    @WorkerThread
    override fun create(name: String, double: Int): Long {
        val player = PlayerTable()
        player.name = name.toLowerCase()
        player.fav = double.toString()
        return playerDao.create(player)
    }

    @WorkerThread
    override fun fetchById(id: Long): Player? {
        val table = playerDao.fetchById(id) ?: return null
        return mapper.to(table)
    }

    @WorkerThread
    override fun fetchByName(name: String): Player? {
        val table = playerDao.fetchByName(name) ?: return null
        return mapper.to(table)
    }

    @WorkerThread
    override fun fetchAll(): List<Player> {
        val table = playerDao.fetchAll()
        return mapper.to(table)
    }
}