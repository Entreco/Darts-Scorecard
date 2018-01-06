package nl.entreco.domain.setup.game

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.TeamIdsString
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(modelCreate: CreateGameRequest, teamIds: TeamIdsString, done: (CreateGameResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val (score, index, legs, sets) = modelCreate
            val id = gameRepository.create(teamIds.toString(), score, index, legs, sets)
            onUi { done(CreateGameResponse(id, teamIds, modelCreate)) }
        }, fail)
    }
}