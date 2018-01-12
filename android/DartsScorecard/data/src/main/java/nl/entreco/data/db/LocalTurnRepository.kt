package nl.entreco.data.db

import android.support.annotation.WorkerThread
import nl.entreco.data.db.turn.TurnMapper
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository

/**
 * Created by Entreco on 23/12/2017.
 */
class LocalTurnRepository(db: DscDatabase, private val mapper: TurnMapper) : TurnRepository {

    private val turnDao = db.turnDao()

    @WorkerThread
    override fun store(gameId: Long, turn: Turn): Long {
        val table = mapper.from(gameId, turn)
        return turnDao.create(table)
    }

    @WorkerThread
    override fun fetchTurnsForGame(gameId: Long): List<Turn> {
        val turnsInGame = turnDao.fetchAll(gameId)
        return mapper.to(turnsInGame)
    }
}