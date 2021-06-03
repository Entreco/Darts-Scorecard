package nl.entreco.domain.setup.game

import nl.entreco.domain.Analytics
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class CreateGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val analytics: Analytics, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(request: CreateGameRequest, teamIdString: String, done: (CreateGameResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            analytics.trackAchievement("Game Created")
            val (score, index, legs, sets) = request
            val id = gameRepository.create(teamIdString, score, index, legs, sets)
            onUi { done(CreateGameResponse(id, teamIdString, score, index, legs, sets)) }
        }, fail)
    }
}