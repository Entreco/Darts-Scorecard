package nl.entreco.domain.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.play.model.Game

/**
 * Created by Entreco on 15/11/2017.
 */
interface GameRepository {

    @Throws
    @WorkerThread
    fun create(uid: String,
               numLegs: Int,
               numSets: Int,
               startIndex: Int,
               startScore: Int): Long

    @Throws
    @WorkerThread
    fun fetchBy(uid: String): Game

    @Throws
    @WorkerThread
    fun fetchLatest(): Game
}