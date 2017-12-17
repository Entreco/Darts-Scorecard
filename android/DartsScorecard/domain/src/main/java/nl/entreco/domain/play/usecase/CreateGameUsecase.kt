package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.Arbiter
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.TurnHandler
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    interface Callback {
        fun onGameCreated(game: Game, setup: CreateGameInput)
        fun onGameRetrieved(game: Game, setup: CreateGameInput)
        fun onGameRetrieveFailed(err: Throwable)
        fun onGameCreateFailed(err: Throwable)
    }

    fun start(createModel: CreateGameInput, teams: TeamsString, callback: Callback) {
        bg.post(Runnable {
            try {
                val (score, index, legs, sets) = createModel
                val id = gameRepository.create(teams.toString(), score, index, legs, sets)
                val game = modelToGame(id, createModel, teams)
                postOnUi({ callback.onGameCreated(game, createModel) })
            } catch (oops: Exception) {
                postOnUi({ callback.onGameCreateFailed(oops) })
            }
        })
    }

    fun fetchLatest(createModel: CreateGameInput, callback: Callback) {
        bg.post(Runnable {
            try {
                val game = gameRepository.fetchLatest()
                fg.post(Runnable {
                    callback.onGameRetrieved(game, createModel)
                })
            } catch (ohno: Exception) {
                fg.post(Runnable { callback.onGameRetrieveFailed(ohno) })
            }
        })
    }

    private fun postOnUi(callable: () -> Unit) {
        fg.post(Runnable {
            callable()
        })
    }

    private fun modelToGame(id: Long, createModel: CreateGameInput, teams: TeamsString): Game {
        val turnHandler = TurnHandler(teams.toTeams(), createModel.startIndex)
        val settings = ScoreSettings(createModel.startScore, createModel.numLegs, createModel.numSets)
        val initial = Score(createModel.startScore, 0, 0, settings)
        val arbiter = Arbiter(initial, turnHandler)
        return Game(id, arbiter)
    }
}