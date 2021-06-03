package nl.entreco.domain.play.start

import nl.entreco.domain.Analytics
import nl.entreco.libcore.BaseUsecase
import nl.entreco.domain.repository.GameRepository
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

class DeleteGameUsecase @Inject constructor(private val gameRepository: GameRepository, private val analytics: Analytics, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground) : nl.entreco.libcore.BaseUsecase(bg, fg) {
    fun exec(request: DeleteGameRequest, done: ()->Unit) {
        onBackground({
            analytics.trackAchievement("Game Abandoned & Deleted")
            gameRepository.delete(request.gameId)
            onUi { done() }
        }, { onUi { done() }})
    }
}