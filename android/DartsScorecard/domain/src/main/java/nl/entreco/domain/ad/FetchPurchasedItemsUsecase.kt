package nl.entreco.domain.ad

import nl.entreco.domain.repository.GameRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

class FetchPurchasedItemsUsecase @Inject constructor(
    private val gameRepository: GameRepository,
    bg: Background, fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun exec(done: (FetchPurchasedItemsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val numberOfGames = gameRepository.countFinishedGames()
            onUi {
                done(FetchPurchasedItemsResponse(numberOfGames >= 1))
            }
        }, fail)
    }
}