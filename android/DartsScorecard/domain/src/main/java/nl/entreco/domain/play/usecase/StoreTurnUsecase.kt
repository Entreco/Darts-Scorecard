package nl.entreco.domain.play.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.StoreTurnRequest
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by Entreco on 23/12/2017.
 */
class StoreTurnUsecase @Inject constructor(private val turnRepository: TurnRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(req: StoreTurnRequest) {
        onBackground(
                { turnRepository.store(req.gameId, req.turn) },
                {})
    }
}