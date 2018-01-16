package nl.entreco.data.db

import nl.entreco.data.db.meta.MetaDao
import nl.entreco.data.db.meta.MetaTable
import nl.entreco.domain.model.TurnMeta
import nl.entreco.domain.repository.MetaRepository

/**
 * Created by entreco on 10/01/2018.
 */
class LocalMetaRepository(db: DscDatabase) : MetaRepository {
    private val metaDao: MetaDao = db.metaDao()

    override fun create(turnId: Long, gameId: Long, stat: TurnMeta, atCheckout: Int): Long {
        val table = MetaTable()
        table.turnId = turnId
        table.gameId = gameId
        table.playerId = stat.playerId
        table.legNumber = stat.score.leg
        table.setNumber = stat.score.set
        table.turnInLeg = stat.num
        table.score = stat.score.score
        table.atCheckout = atCheckout

        // Sample Queries:
        // select (number * multiplier) as scored FROM TurnMeta where Player=2
        // select  turn, (number * multiplier) as scored FROM TurnMeta where Player=3 group by turn

        return metaDao.create(table)
    }
}