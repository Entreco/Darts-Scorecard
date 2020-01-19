package nl.entreco.domain.repository

import androidx.annotation.WorkerThread
import nl.entreco.domain.model.players.Bot

interface BotRepository {

    @Throws
    @WorkerThread
    fun create(name: String, level: Float): Long

    @Throws
    @WorkerThread
    fun fetchAll(): List<Bot>

    @Throws
    @WorkerThread
    fun fetchByName(name: String): Bot?

    @Throws
    @WorkerThread
    fun fetchById(id: Long): Bot?
}