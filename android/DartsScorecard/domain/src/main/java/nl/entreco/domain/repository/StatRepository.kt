package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.model.Stats

/**
 * Created by entreco on 10/01/2018.
 */
interface StatRepository {
    @Throws
    @WorkerThread
    fun create(playerId: Long, turnId: Long, gameId: Long, stat: Stats): Long
}