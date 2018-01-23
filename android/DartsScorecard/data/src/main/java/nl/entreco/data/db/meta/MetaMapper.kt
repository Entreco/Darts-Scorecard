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

    fun from(gameId: Long, turnId: Long, turnMeta: TurnMeta, atDouble: Int): MetaTable {
        val table = MetaTable()
        table.turnId = turnId
        table.gameId = gameId
        table.playerId = turnMeta.playerId
        table.legNumber = turnMeta.score.leg
        table.setNumber = turnMeta.score.set
        table.turnInLeg = turnMeta.turnNumber
        table.score = turnMeta.score.score
        table.atCheckout = atDouble
        table.started = turnMeta.started
        table.breakMade = turnMeta.breakMade
        return table
    }
}