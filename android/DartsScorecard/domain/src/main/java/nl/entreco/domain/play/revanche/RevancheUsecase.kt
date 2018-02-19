package nl.entreco.domain.play.revanche

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by entreco on 19/02/2018.
 */
class RevancheUsecase @Inject constructor(
        private val gameRepository: GameRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun recreateGameAndStart(request: RevancheRequest, done: (RevancheResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val (_, teams, score, _, legs, sets) = request.originalRequest
            val index = request.newStartIndex
            val id = gameRepository.create(teams, score, index, legs, sets)
            val game = gameRepository.fetchBy(id)
            val scoreSettings = ScoreSettings(score, legs, sets, index)
            onUi{ done(RevancheResponse(game, scoreSettings, request.teams, teams)) }
        }, fail)
    }
}
