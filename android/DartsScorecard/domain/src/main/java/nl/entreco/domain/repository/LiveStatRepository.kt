package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.model.LiveStat

/**
 * Created by entreco on 16/01/2018.
 */
interface LiveStatRepository {

    @WorkerThread
    fun fetchAllForGame(gameId: Long): Map<Long, LiveStat>

    @WorkerThread
    fun fetchStat(turnId: Long, metaId: Long): LiveStat
}