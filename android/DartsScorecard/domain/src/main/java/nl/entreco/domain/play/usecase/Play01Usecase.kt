package nl.entreco.domain.play.usecase

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.RetrieveGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class Play01Usecase @Inject constructor(private val retrieveGameUsecase: RetrieveGameUsecase,
                                        private val retrieveTeamsUsecase: RetrieveTeamsUsecase) {

    fun loadGameAndStart(req: RetrieveGameRequest, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeams(req, done, fail)
    }

    private fun retrieveTeams(req: RetrieveGameRequest, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeamsUsecase.exec(req.teamIds,
                { teams -> retrieveGame(req.gameId, req.create.startIndex, teams, done, fail) },
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