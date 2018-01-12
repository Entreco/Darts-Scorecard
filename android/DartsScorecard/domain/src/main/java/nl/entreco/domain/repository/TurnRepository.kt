package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 23/12/2017.
 */
interface TurnRepository {

    @WorkerThread
    fun fetchTurnsForGame(gameId: Long): List<Turn>

    @WorkerThread
    fun store(gameId: Long, turn: Turn): Long
}