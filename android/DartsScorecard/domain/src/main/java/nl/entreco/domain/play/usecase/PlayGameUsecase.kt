package nl.entreco.domain.play.usecase

import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.players.Team
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class PlayGameUsecase @Inject constructor(private val retrieveGameUsecase: RetrieveGameUsecase,
                                          private val retrieveTeamsUsecase: RetrieveTeamsUsecase) {

    fun loadGameAndStart(req: RetrieveGameRequest, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeams(req, done, fail)
    }

    private fun retrieveTeams(req: RetrieveGameRequest, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeamsUsecase.start(req.teamIds,
                { teams -> retrieveGame(req.gameId, req.settings.startIndex, teams, done, fail) },
                { err -> fail(err) }
        )
    }

    private fun retrieveGame(gameId: Long, startIndex: Int, teams: Array<Team>, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveGameUsecase.start(gameId,
                { game ->
                    game.start(startIndex, teams)
                    done.invoke(game, teams)
                },
                { err -> fail.invoke(err) }
        )
    }
}