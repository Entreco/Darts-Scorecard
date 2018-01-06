package nl.entreco.domain.launch.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(modelCreate: CreateGameRequest, teamIds: TeamIdsString, done: (RetrieveGameRequest) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val (score, index, legs, sets) = modelCreate
            val id = gameRepository.create(teamIds.toString(), score, index, legs, sets)
            onUi { done(RetrieveGameRequest(id, teamIds, modelCreate)) }
        }, fail)
    }
}