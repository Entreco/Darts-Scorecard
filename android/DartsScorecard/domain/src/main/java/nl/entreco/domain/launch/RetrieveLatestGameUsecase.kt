package nl.entreco.domain.launch

import nl.entreco.domain.repository.GameRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
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