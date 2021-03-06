package nl.entreco.domain.launch

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
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