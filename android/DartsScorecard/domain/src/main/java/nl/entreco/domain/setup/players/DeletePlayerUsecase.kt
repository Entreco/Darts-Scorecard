package nl.entreco.domain.setup.players

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.PlayerRepository
import javax.inject.Inject

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerUsecase @Inject constructor(private val playerRepository: PlayerRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun delete(request: DeletePlayerRequest, done: (DeletePlayerResponse)->Unit, fail: (Throwable)->Unit){
        onBackground({
            playerRepository.deleteById(request.id)
            onUi { done(DeletePlayerResponse(request.id)) }
        }, fail)
    }
}