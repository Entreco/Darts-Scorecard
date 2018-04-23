package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.model.LiveStat

/**
 * Created by entreco on 16/01/2018.
 */
interface StatRepository {

    @WorkerThread
    fun fetchAllForGame(gameId: Long): Map<Long, LiveStat>

    @WorkerThread
    fun fetchStat(turnId: Long, metaId: Long): LiveStat
}