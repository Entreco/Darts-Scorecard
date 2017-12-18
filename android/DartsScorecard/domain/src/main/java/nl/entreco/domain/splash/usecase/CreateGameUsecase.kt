package nl.entreco.domain.splash.usecase

import nl.entreco.domain.executors.Background
import nl.entreco.domain.executors.Foreground
import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val bg: Background, private val fg: Foreground) {

    fun start(modelCreate: CreateGameRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        bg.post(Runnable {
            try {
                val (score, index, legs, sets) = modelCreate
                val id = gameRepository.create(teamIds.toString(), score, index, legs, sets)
                postOnUi({ done(RetrieveGameRequest(id, teamIds, modelCreate)) })
            } catch (oops: Exception) {
                postOnUi({ fail(oops) })
            }
        })
    }

    fun fetchLatest(modelCreate: CreateGameRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        bg.post(Runnable {
            try {
                val game = gameRepository.fetchLatest()
                fg.post(Runnable {
                    done(RetrieveGameRequest(game.id, teamIds, modelCreate))
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