package nl.entreco.data.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository

/**
 * Created by Entreco on 23/12/2017.
 */
class LocalTurnRepository(db: DscDatabase, private val mapper: TurnMapper) : TurnRepository {

    private val turnDao = db.turnDao()

    @WorkerThread
    override fun store(gameId: Long, turn: Turn) {
        val table = mapper.from(gameId, turn)
        turnDao.create(table)
    }

    @WorkerThread
    override fun fetchTurnsForGame(gameId: Long): List<Turn> {
        val turnsInGame = turnDao.fetchAll(gameId)
        return mapper.to(turnsInGame)
    }
}