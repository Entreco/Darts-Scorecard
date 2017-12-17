package nl.entreco.domain.play.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    interface Callback {
        fun onGameCreated(setup: RetrieveGameRequest)
        fun onGameRetrieved(setup: RetrieveGameRequest)
        fun onGameRetrieveFailed(err: Throwable, teamIds: TeamIdsString)
        fun onGameCreateFailed(err: Throwable)
    }

    fun start(modelSettings: GameSettingsRequest, teamIds: TeamIdsString, callback: Callback) {
        bg.post(Runnable {
            try {
                val (score, index, legs, sets) = modelSettings
                val id = gameRepository.create(teamIds.toString(), score, index, legs, sets)
                postOnUi({ callback.onGameCreated(RetrieveGameRequest(id, teamIds, modelSettings)) })
            } catch (oops: Exception) {
                postOnUi({ callback.onGameCreateFailed(oops) })
            }
        })
    }

    fun fetchLatest(modelSettings: GameSettingsRequest, teamIds: TeamIdsString, callback: Callback) {
        bg.post(Runnable {
            try {
                val game = gameRepository.fetchLatest()
                fg.post(Runnable {
                    callback.onGameRetrieved(RetrieveGameRequest(game.id, teamIds, modelSettings))
                })
            } catch (ohno: Exception) {
                fg.post(Runnable { callback.onGameRetrieveFailed(ohno, teamIds) })
            }
        })
    }

    private fun postOnUi(callable: () -> Unit) {
        fg.post(Runnable {
            callable()
        })
    }
}