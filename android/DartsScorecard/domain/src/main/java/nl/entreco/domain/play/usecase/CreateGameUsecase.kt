package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import java.util.*
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    interface Callback {
        fun onGameCreated(game: Game, setup: SetupModel)
        fun onGameFailed(err: Throwable)
    }

    fun start(createModel: SetupModel, callback: Callback) {
        bg.post(Runnable {

            val game = modelToGame(createModel)
            val (score, index, legs, sets) = createModel
            gameRepository.create(game.uuid, score, index, legs, sets)
            postOnUi(callback, game, createModel)
        })
    }

    private fun postOnUi(callback: Callback, game: Game, createModel: SetupModel) {
        fg.post(Runnable {
            callback.onGameCreated(game, createModel)
        })
    }

    private fun modelToGame(createModel: SetupModel): Game {
        val teams = arrayOf(Team(arrayOf(Player("remco"), Player("sibbel"))), Team(arrayOf(Player("Boeffie"))), Team(arrayOf(Player("eva"), Player("guusje"), Player("Beer"))))
        val turnHandler = TurnHandler(teams, createModel.startIndex)
        val settings = ScoreSettings(createModel.startScore, createModel.numLegs, createModel.numSets)
        val initial = Score(createModel.startScore, 0, 0, settings)
        val arbiter = Arbiter(initial, turnHandler)
        return Game(UUID.randomUUID().toString(), arbiter)
    }
}