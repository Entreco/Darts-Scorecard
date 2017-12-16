package nl.entreco.data.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.data.DscDatabase
import nl.entreco.data.GameDao
import nl.entreco.data.GameTable
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import java.util.*

/**
 * Created by Entreco on 15/11/2017.
 */
class LocalGameRepository(db: DscDatabase) : GameRepository {

    private val gameDao: GameDao = db.gameDao()

    @WorkerThread
    override fun create(uid: String, numLegs: Int, numSets: Int, startIndex: Int, startScore: Int): Long {
        val table = GameTable()

        table.uid = uid
        table.numLegs = numLegs
        table.numSets = numSets
        table.startIndex = startIndex
        table.startScore = startScore

        return gameDao.create(table)
    }

    @WorkerThread
    override fun fetchBy(uid: String): Game {
        val gameTable = gameDao.fetchBy(uid)
        return toGame(gameTable)
    }

    @WorkerThread
    override fun fetchLatest(): Game {
        val all = gameDao.fetchAll()
        return if (all.isEmpty()) throw IllegalStateException("game not found")
        else toGame(all[0])
    }

    private fun toGame(gameTable: GameTable): Game {
        val uid = gameTable.uid
        val startIndex = gameTable.startIndex
        val startScore = gameTable.startScore
        val legs = gameTable.numLegs
        val sets = gameTable.numSets
        val teams = arrayOf(Team(arrayOf(Player("remco"), Player("eva"))), Team(arrayOf(Player("henkie"))), Team(arrayOf(Player("Guusje"), Player("De beestenboel"))))

        val setting = ScoreSettings(startScore, legs, sets, startIndex)
        val initial = Score(startScore, 0, 0, setting)
        val arbiter = Arbiter(initial, TurnHandler(teams, startIndex))
        return Game(uid, arbiter)
    }
}