package nl.entreco.domain.play.stats

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by Entreco on 23/12/2017.
 */
class StoreTurnUsecase @Inject constructor(private val turnRepository: TurnRepository,
                                           bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {
    fun exec(req: StoreTurnRequest, done: (StoreTurnResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val turnId = turnRepository.store(req.gameId, req.playerId, if(req.state != State.ERR_BUST) req.turn else Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO))
            onUi { done(StoreTurnResponse(turnId, req.turn)) }
        }, fail)
    }
}