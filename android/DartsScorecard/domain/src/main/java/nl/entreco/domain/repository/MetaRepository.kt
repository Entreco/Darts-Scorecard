package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.model.TurnMeta

/**
 * Created by entreco on 10/01/2018.
 */
interface MetaRepository {
    @Throws
    @WorkerThread
    fun create(turnId: Long, gameId: Long, stat: TurnMeta, atDouble: Int): Long
}