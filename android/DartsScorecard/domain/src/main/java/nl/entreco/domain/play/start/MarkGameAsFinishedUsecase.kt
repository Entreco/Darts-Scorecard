package nl.entreco.domain.play.start

import nl.entreco.domain.Analytics
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by entreco on 09/01/2018.
 */
class MarkGameAsFinishedUsecase @Inject constructor(private val gameRepository: GameRepository, private val analytics: Analytics, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground) : nl.entreco.libcore.BaseUsecase(bg, fg) {
    fun exec(request: MarkGameAsFinishedRequest) {
        onBackground({
            analytics.trackAchievement("Game Finished")
            gameRepository.finish(request.gameId, request.winningTeam)
        }, {})
    }
}