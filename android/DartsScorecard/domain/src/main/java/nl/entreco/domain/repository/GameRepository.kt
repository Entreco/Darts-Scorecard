package nl.entreco.domain.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.model.Game

/**
 * Created by Entreco on 15/11/2017.
 */
interface GameRepository {

    @Throws
    @WorkerThread
    fun create(
            teams: String,
            startScore: Int,
            startIndex: Int,
            numLegs: Int,
            numSets: Int): Long

    @WorkerThread
    fun finish(id: Long)

    @WorkerThread
    fun undoFinish(id: Long)

    @Throws
    @WorkerThread
    fun fetchBy(id: Long): Game

    @Throws
    @WorkerThread
    fun fetchLatest(): FetchLatestGameResponse
}