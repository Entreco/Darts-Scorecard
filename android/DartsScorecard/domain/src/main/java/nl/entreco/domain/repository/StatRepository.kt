package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.model.Stat

/**
 * Created by entreco on 16/01/2018.
 */
interface StatRepository {

    @WorkerThread
    fun fetchAllForGame(gameId: Long): Map<Long, Stat>

    @WorkerThread
    fun fetchStat(turnId: Long, metaId: Long): Stat
}