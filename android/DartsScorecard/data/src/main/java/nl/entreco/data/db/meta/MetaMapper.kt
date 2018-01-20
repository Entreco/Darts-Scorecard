package nl.entreco.data.db.meta

import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.TurnMeta

/**
 * Created by entreco on 20/01/2018.
 */
class MetaMapper : Mapper<TurnMeta, MetaTable> {

    override fun to(from: TurnMeta): MetaTable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun from(gameId: Long, turnId: Long, stat: TurnMeta): MetaTable {
        val table = MetaTable()
        table.turnId = turnId
        table.gameId = gameId
        table.playerId = stat.playerId
        table.legNumber = stat.score.leg
        table.setNumber = stat.score.set
        table.turnInLeg = stat.num
        table.score = stat.score.score
        table.atCheckout = stat.atDouble
        return table
    }
}