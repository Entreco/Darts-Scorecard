package nl.entreco.data.play.repository

import nl.entreco.data.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository

/**
 * Created by Entreco on 23/12/2017.
 */
class LocalTurnRepository(db: DscDatabase, private val mapper: Mapper<TurnTable, Turn>) : TurnRepository {

    private val turnDao = db.turnDao()

    override fun store(gameId: Long, turn: Turn) {
        val table = TurnTable()
        table.game = gameId
        table.d1 = turn.first().value()
        table.d2 = turn.second().value()
        table.d3 = turn.third().value()
        table.numDarts = 3 - turn.dartsLeft()
        turnDao.create(table)
    }

    override fun fetchTurnsForGame(gameId: Long): List<Turn> {
        val turnsInGame = turnDao.fetchAll(gameId)
        return mapper.to(turnsInGame)
    }
}