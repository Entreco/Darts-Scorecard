package nl.entreco.data.db.stats

import android.support.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.Stat
import nl.entreco.domain.repository.StatRepository

/**
 * Created by entreco on 16/01/2018.
 */
class LocalStatRepository(db: DscDatabase, private val mapper: StatMapper) : StatRepository {

    private val turnDao = db.turnDao()
    private val metaDao = db.metaDao()

    @WorkerThread
    override fun fetchAllForGame(gameId: Long): Map<Long, Stat> {
        val turnTable = turnDao.fetchAll(gameId)
        val metaTable = metaDao.fetchAll(gameId)
        return mapper.to(turnTable, metaTable)
    }

    @WorkerThread
    override fun fetchStat(turnId: Long, metaId: Long): Stat {
        val turnTable = turnDao.fetchById(turnId)
        val metaTable = metaDao.fetchById(metaId)
        return mapper.to(turnTable, metaTable)
    }
}