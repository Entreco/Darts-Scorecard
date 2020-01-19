package nl.entreco.domain.play.start

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by Entreco on 23/12/2017.
 */
class RetrieveTurnsUsecase @Inject constructor(private val turnRepository: TurnRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(request: RetrieveTurnsRequest, done: (RetrieveTurnsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val turns = turnRepository.fetchTurnsForGame(request.gameId)
            onUi { done(RetrieveTurnsResponse(turns)) }
        }, fail)
    }
}