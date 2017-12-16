package nl.entreco.data.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameMapper
import nl.entreco.data.db.game.GameTable
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.repository.GameRepository

/**
 * Created by Entreco on 15/11/2017.
 */
class LocalGameRepository(db: DscDatabase, private var mapper: GameMapper) : GameRepository {

    private val gameDao: GameDao = db.gameDao()

    @Throws
    @WorkerThread
    override fun create(uid: String, teams: String, startScore: Int, startIndex: Int, numLegs: Int, numSets: Int): Long {
        val table = GameTable()

        table.uid = uid
        table.teams = teams
        table.numLegs = numLegs
        table.numSets = numSets
        table.startIndex = startIndex
        table.startScore = startScore

        return gameDao.create(table)
    }

    @WorkerThread
    override fun fetchBy(uid: String): Game {
        val gameTable = gameDao.fetchBy(uid) ?: throw IllegalStateException("Game $uid does not exist")
        return mapper.to(gameTable)
    }

    @WorkerThread
    override fun fetchLatest(): Game {
        val all = gameDao.fetchAll()
        return if (all.isEmpty()) throw IllegalStateException("no Games found")
        else mapper.to(all[0])
    }
}