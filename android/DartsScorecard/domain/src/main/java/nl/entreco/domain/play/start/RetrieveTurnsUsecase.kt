package nl.entreco.domain.play.start

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by Entreco on 23/12/2017.
 */
class RetrieveTurnsUsecase @Inject constructor(private val turnRepository: TurnRepository, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground) : nl.entreco.libcore.BaseUsecase(bg, fg) {
    fun exec(request: RetrieveTurnsRequest, done: (RetrieveTurnsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val turns = turnRepository.fetchTurnsForGame(request.gameId)
            onUi { done(RetrieveTurnsResponse(turns)) }
        }, fail)
    }
}