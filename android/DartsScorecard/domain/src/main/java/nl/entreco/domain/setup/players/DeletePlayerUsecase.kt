package nl.entreco.domain.setup.players

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerUsecase @Inject constructor(private val playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun delete(request: DeletePlayerRequest, done: (DeletePlayerResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            request.ids.forEach { playerRepository.deleteById(it) }
            onUi { done(DeletePlayerResponse(request.ids)) }
        }, fail)
    }
}