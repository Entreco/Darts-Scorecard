package nl.entreco.data.play.repository

import android.support.annotation.WorkerThread
import nl.entreco.data.DscDatabase
import nl.entreco.data.GameDao
import nl.entreco.data.GameTable
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.play.usecase.SetupModel
import nl.entreco.domain.settings.ScoreSettings
import java.util.*

/**
 * Created by Entreco on 15/11/2017.
 */
class LocalGameRepository(db: DscDatabase) : GameRepository {

    private val gameDao: GameDao = db.gameDao()

    @WorkerThread
    override fun create(createModel: SetupModel): Game {
        val teams = arrayOf(Team(arrayOf(Player("remco"), Player("sibbel"))), Team(arrayOf(Player("Boeffie"))), Team(arrayOf(Player("eva"), Player("guusje"), Player("Beer"))))
        val turnHandler = TurnHandler(teams, createModel.startIndex)
        val settings = ScoreSettings(createModel.startScore, createModel.numLegs, createModel.numSets)
        val initial = Score(createModel.startScore, 0, 0, settings)
        val arbiter = Arbiter(initial, turnHandler)
        val game = Game(arbiter)

        gameDao.create(toGameTable(game))
        return game
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
        return Game(arbiter)
    }

    private fun toGameTable(game: Game): GameTable {
        val table = GameTable()
        table.uid = UUID.randomUUID().toString()
        table.numLegs = 3
        table.numSets = 2
        table.startIndex = 0
        table.startScore = 501
        return table
    }
}