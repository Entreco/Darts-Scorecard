package nl.entreco.domain.setup.game

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(request: CreateGameRequest, teamIdString: String, done: (CreateGameResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val (score, index, legs, sets) = request
            val id = gameRepository.create(teamIdString, score, index, legs, sets)
            onUi { done(CreateGameResponse(id, teamIdString, score, index, legs, sets)) }
        }, fail)
    }
}