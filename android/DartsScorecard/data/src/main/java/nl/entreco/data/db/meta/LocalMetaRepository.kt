package nl.entreco.data.db.meta

import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.TurnMeta
import nl.entreco.domain.repository.MetaRepository

/**
 * Created by entreco on 10/01/2018.
 */
class LocalMetaRepository(db: DscDatabase, private val mapper: MetaMapper) : MetaRepository {
    private val metaDao: MetaDao = db.metaDao()

    override fun create(turnId: Long, gameId: Long, stat: TurnMeta, atDouble: Int): Long {
        val table = mapper.from(gameId, turnId, stat,atDouble)
        return metaDao.create(table)
    }
}