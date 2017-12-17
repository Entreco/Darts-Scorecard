package nl.entreco.domain.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.domain.play.model.players.Player

/**
 * Created by Entreco on 16/12/2017.
 */
interface PlayerRepository {
    @Throws
    @WorkerThread
    fun create(name: String,
               double: Int): Long

    @Throws
    @WorkerThread
    fun fetchById(id: Long): Player?

    @Throws
    @WorkerThread
    fun fetchByName(name: String): Player?

    @Throws
    @WorkerThread
    fun fetchAll(): List<Player>
}