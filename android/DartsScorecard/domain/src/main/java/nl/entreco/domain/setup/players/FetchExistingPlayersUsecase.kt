package nl.entreco.domain.setup.players

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 02/01/2018.
 */
class FetchExistingPlayersUsecase @Inject constructor(private var playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(done: (FetchExistingPlayersResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val players = playerRepository.fetchAll()
            onUi { done(FetchExistingPlayersResponse(players)) }
        }, fail)
    }
}