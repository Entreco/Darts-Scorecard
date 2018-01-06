package nl.entreco.domain.play.usecase

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.model.Turn
import nl.entreco.domain.repository.TurnRepository
import javax.inject.Inject

/**
 * Created by Entreco on 23/12/2017.
 */
class RetrieveTurnsUsecase @Inject constructor(private val turnRepository: TurnRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun exec(gameId: Long, done: (Array<Turn>) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val turns = turnRepository.fetchTurnsForGame(gameId)
            onUi { done(turns.toTypedArray()) }
        }, fail)
    }
}