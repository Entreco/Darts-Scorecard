package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 23/12/2017.
 */
interface TurnRepository {

    @WorkerThread
    fun fetchTurnsForGame(gameId: Long): List<Pair<Long, Turn>>

    @WorkerThread
    fun store(gameId: Long, playerId: Long, turn: Turn): Long

    @WorkerThread
    fun undo(gameId: Long): Int
}