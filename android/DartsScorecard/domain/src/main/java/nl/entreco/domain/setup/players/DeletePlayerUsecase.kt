package nl.entreco.domain.setup.players

import nl.entreco.domain.repository.PlayerRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerUsecase @Inject constructor(
    private val playerRepository: PlayerRepository, bg: Background, fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun delete(request: DeletePlayerRequest, done: (DeletePlayerResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            request.ids.forEach { playerRepository.deleteById(it) }
            onUi { done(DeletePlayerResponse(request.ids)) }
        }, fail)
    }
}