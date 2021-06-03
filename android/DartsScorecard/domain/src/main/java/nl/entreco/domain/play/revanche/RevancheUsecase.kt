package nl.entreco.domain.play.revanche

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by entreco on 19/02/2018.
 */
class RevancheUsecase @Inject constructor(
    private val gameRepository: GameRepository, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun recreateGameAndStart(request: RevancheRequest, done: (RevancheResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val (_, teams, score, _, legs, sets) = request.originalRequest
            val index = request.newStartIndex
            val id = gameRepository.create(teams, score, index, legs, sets)
            val game = gameRepository.fetchBy(id)
            val scoreSettings = ScoreSettings(score, legs, sets, index)

            //
            game.start(index, request.teams)

            onUi{ done(RevancheResponse(game, scoreSettings, request.teams, teams)) }
        }, fail)
    }
}
