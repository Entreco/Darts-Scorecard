package nl.entreco.domain.play.usecase

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.StoreTurnRequest
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class Play01Usecase @Inject constructor(private val retrieveGameUsecase: RetrieveGameUsecase,
                                        private val retrieveTurnsUsecase: RetrieveTurnsUsecase,
                                        private val retrieveTeamsUsecase: RetrieveTeamsUsecase,
                                        private val storeTurnUsecase: StoreTurnUsecase) {

    fun loadGameAndStart(req: RetrieveGameRequest, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeams(req, done, fail)
    }

    fun storeTurn(req: StoreTurnRequest){
        storeTurnUsecase.exec(req)
    }

    private fun retrieveTeams(req: RetrieveGameRequest, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeamsUsecase.exec(req.teamIds,
                { teams -> retrieveGame(req.gameId, req.create.startIndex, teams, done, fail) },
                { err -> fail(err) }
        )
    }

    private fun retrieveGame(gameId: Long, startIndex: Int, teams: Array<Team>, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveGameUsecase.start(gameId,
                { game -> retrieveTurns(game, startIndex, teams, done, fail) },
                { err -> fail(err) }
        )
    }

    private fun retrieveTurns(game: Game, startIndex: Int, teams: Array<Team>, done: (Game, Array<Team>) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTurnsUsecase.exec(game.id,
                { turns ->
                    game.start(startIndex, teams)
                    turns.forEach { game.handle(it) }
                    done.invoke(game, teams)
                }, { err -> fail(err) })
    }
}