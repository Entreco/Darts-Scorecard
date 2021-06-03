package nl.entreco.domain.setup.players

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by Entreco on 02/01/2018.
 */
class FetchExistingPlayersUsecase @Inject constructor(
    private var playerRepository: PlayerRepository,
    bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(done: (FetchExistingPlayersResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val players = playerRepository.fetchAll()
            onUi { done(FetchExistingPlayersResponse(players)) }
        }, fail)
    }
}