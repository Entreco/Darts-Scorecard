package nl.entreco.data.db.stats

import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.Stat
import nl.entreco.domain.repository.StatRepository

/**
 * Created by entreco on 16/01/2018.
 */
class LocalStatRepository(db: DscDatabase, private val mapper: StatMapper) : StatRepository {

    private val turnDao = db.turnDao()
    private val metaDao = db.metaDao()

    override fun fetchAllForGame(gameId: Long): Map<Long, Stat> {
        val turnTable = turnDao.fetchAll(gameId)
        val metaTable = metaDao.fetchAll(gameId)
        return mapper.to(turnTable, metaTable)
    }

    override fun fetchAllForPlayer(playerId: Long): Map<Long, Stat> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}