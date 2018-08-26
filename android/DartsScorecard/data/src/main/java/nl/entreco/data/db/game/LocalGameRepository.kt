package nl.entreco.data.db.game

import android.support.annotation.WorkerThread
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.Mapper
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
    override fun finish(id: Long, winningTeam: String) {
        val gameTable = gameDao.fetchBy(id)!!
        gameTable.finished = true
        gameTable.winningTeam = winningTeam
        gameDao.updateGames(gameTable)
    }

    @WorkerThread
    override fun undoFinish(id: Long) {
        val gameTable = gameDao.fetchBy(id)!!
        gameTable.finished = false
        gameDao.undoFinish(gameTable)
    }

    @Throws
    @WorkerThread
    override fun fetchBy(id: Long): Game {
        val gameTable = gameDao.fetchBy(id)
                ?: throw IllegalStateException("Game $id does not exist")
        return mapper.to(gameTable)
    }

    @Throws
    @WorkerThread
    override fun fetchLatest(): FetchLatestGameResponse {
        val allUnfinishedGames = gameDao.fetchAll().filter { !it.finished }
        return if (allUnfinishedGames.isEmpty()) throw IllegalStateException("no Games found")
        else {
            val latestGame = allUnfinishedGames.first()
            FetchLatestGameResponse(latestGame.id,
                    latestGame.teams,
                    latestGame.startScore,
                    latestGame.startIndex,
                    latestGame.numLegs,
                    latestGame.numSets)
        }
    }

    @WorkerThread
    override fun countFinishedGames(): Int {
        return gameDao.fetchAll().count { it.finished }
    }
}