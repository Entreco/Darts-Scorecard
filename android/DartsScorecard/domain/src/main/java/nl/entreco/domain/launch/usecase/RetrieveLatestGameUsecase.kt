package nl.entreco.domain.launch.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.repository.GameRepository
import javax.inject.Inject

/**
 * Created by Entreco on 19/12/2017.
 */
class RetrieveLatestGameUsecase @Inject constructor(private val gameRepository: GameRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(done: (FetchLatestGameResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val response = gameRepository.fetchLatest()
            onUi { done(response) }
        }, fail)
    }
}