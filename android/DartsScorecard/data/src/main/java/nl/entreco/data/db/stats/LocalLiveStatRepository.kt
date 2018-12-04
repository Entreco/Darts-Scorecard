package nl.entreco.data.db.stats

import androidx.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.LiveStat
import nl.entreco.domain.repository.LiveStatRepository

/**
 * Created by entreco on 16/01/2018.
 */
class LocalLiveStatRepository(db: DscDatabase, private val mapperLive: LiveStatMapper) : LiveStatRepository {

    private val turnDao = db.turnDao()
    private val metaDao = db.metaDao()

    @WorkerThread
    override fun fetchAllForGame(gameId: Long): Map<Long, LiveStat> {
        val turnTable = turnDao.fetchAll(gameId)
        val metaTable = metaDao.fetchAll(gameId)
        return mapperLive.to(turnTable, metaTable)
    }

    @WorkerThread
    override fun fetchStat(turnId: Long, metaId: Long): LiveStat {
        val turnTable = turnDao.fetchById(turnId)
        val metaTable = metaDao.fetchById(metaId)
        return mapperLive.to(turnTable, metaTable)
    }
}