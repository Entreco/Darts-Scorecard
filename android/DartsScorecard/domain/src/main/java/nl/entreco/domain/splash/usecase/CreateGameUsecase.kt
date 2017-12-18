package nl.entreco.domain.splash.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.model.players.TeamIdsString
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    fun start(modelSettings: GameSettingsRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        bg.post(Runnable {
            try {
                val (score, index, legs, sets) = modelSettings
                val id = gameRepository.create(teamIds.toString(), score, index, legs, sets)
                postOnUi({ done(RetrieveGameRequest(id, teamIds, modelSettings)) })
            } catch (oops: Exception) {
                postOnUi({ fail(oops) })
            }
        })
    }

    fun fetchLatest(modelSettings: GameSettingsRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        bg.post(Runnable {
            try {
                val game = gameRepository.fetchLatest()
                fg.post(Runnable {
                    done(RetrieveGameRequest(game.id, teamIds, modelSettings))
                })
            } catch (ohno: Exception) {
                fg.post(Runnable { fail(ohno) })
            }
        })
    }

    private fun postOnUi(callable: () -> Unit) {
        fg.post(Runnable {
            callable()
        })
    }
}