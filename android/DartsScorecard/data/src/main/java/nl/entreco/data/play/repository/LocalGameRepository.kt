package nl.entreco.data.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.data.DscDatabase
import nl.entreco.data.db.Mapper
import nl.entreco.data.db.game.GameDao
import nl.entreco.data.db.game.GameTable
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.model.Game
import nl.entreco.domain.repository.GameRepository

/**
 * Created by Entreco on 15/11/2017.
 */
class LocalGameRepository(db: DscDatabase, private val mapper: Mapper<GameTable, Game>) : GameRepository {

    private val gameDao: GameDao = db.gameDao()

    @Throws
    @WorkerThread
    override fun create(teams: String, startScore: Int, startIndex: Int, numLegs: Int, numSets: Int): Long {
        val table = GameTable()

        table.teams = teams
        table.numLegs = numLegs
        table.numSets = numSets
        table.startIndex = startIndex
        table.startScore = startScore

        return gameDao.create(table)
    }

    @WorkerThread
    override fun fetchBy(id: Long): Game {
        val gameTable = gameDao.fetchBy(id) ?: throw IllegalStateException("Game $id does not exist")
        return mapper.to(gameTable)
    }

    @WorkerThread
    override fun fetchLatest(): FetchLatestGameResponse {
        val all = gameDao.fetchAll()
        return if (all.isEmpty()) throw IllegalStateException("no Games found")
        else {
            val latestGame = all[0]
            FetchLatestGameResponse(latestGame.id, latestGame.teams, latestGame.startScore, latestGame.startIndex, latestGame.numLegs, latestGame.numSets)
        }
    }
}