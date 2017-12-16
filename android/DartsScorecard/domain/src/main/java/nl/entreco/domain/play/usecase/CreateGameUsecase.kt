package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.*
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import java.util.*
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    interface Callback {
        fun onGameCreated(game: Game, setup: CreateGameInput)
        fun onGameFailed(err: Throwable)
    }

    fun start(createModel: CreateGameInput, teams: TeamsString, callback: Callback) {
        bg.post(Runnable {

            try {
                val lastGame = gameRepository.fetchLatest()
                postOnUi({ callback.onGameCreated(lastGame, createModel) })
            } catch (oops: Exception){
                val game = modelToGame(createModel, teams)
                val (score, index, legs, sets) = createModel

                try {
                    gameRepository.create(game.uuid, teams.asString(), score, index, legs, sets)
                    postOnUi({ callback.onGameCreated(game, createModel) })
                } catch (oops: Exception) {
                    postOnUi({ callback.onGameFailed(oops) })
                }
            }
        })
    }

    private fun postOnUi(callable: () -> Unit) {
        fg.post(Runnable {
            callable()
        })
    }

    private fun modelToGame(createModel: CreateGameInput, teams: TeamsString): Game {
        val turnHandler = TurnHandler(teams.asTeamArray(), createModel.startIndex)
        val settings = ScoreSettings(createModel.startScore, createModel.numLegs, createModel.numSets)
        val initial = Score(createModel.startScore, 0, 0, settings)
        val arbiter = Arbiter(initial, turnHandler)
        return Game(UUID.randomUUID().toString(), arbiter)
    }
}