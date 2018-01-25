package nl.entreco.data.db.turn

import android.support.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository

/**
 * Created by Entreco on 23/12/2017.
 */
class LocalTurnRepository(db: DscDatabase, private val mapper: TurnMapper) : TurnRepository {

    private val turnDao = db.turnDao()

    @WorkerThread
    override fun store(gameId: Long, playerId: Long, turn: Turn): Long {
        val table = mapper.from(gameId, playerId, turn)
        return turnDao.create(table)
    }

    @WorkerThread
    override fun fetchTurnsForGame(gameId: Long): List<Pair<Long, Turn>> {
        val map = mutableListOf<Pair<Long, Turn>>()
        val turnsInGame = turnDao.fetchAll(gameId)
        turnsInGame.forEach { map.add(Pair(it.player, mapper.to(it))) }
        return map
    }

    @WorkerThread
    override fun undo(gameId: Long): Int {
        return turnDao.undoLast(gameId)
    }
}