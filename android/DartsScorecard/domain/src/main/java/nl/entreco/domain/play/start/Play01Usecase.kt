package nl.entreco.domain.play.start

import nl.entreco.domain.play.stats.StoreTurnRequest
import nl.entreco.domain.play.stats.StoreTurnUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class Play01Usecase @Inject constructor(private val retrieveGameUsecase: RetrieveGameUsecase,
                                        private val retrieveTurnsUsecase: RetrieveTurnsUsecase,
                                        private val retrieveTeamsUsecase: RetrieveTeamsUsecase,
                                        private val storeTurnUsecase: StoreTurnUsecase) {

    fun loadGameAndStart(req: Play01Request, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeams(req, done, fail)
    }

    fun storeTurn(req: StoreTurnRequest) {
        storeTurnUsecase.exec(req)
    }

    private fun retrieveTeams(req: Play01Request, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeamsUsecase.exec(RetrieveTeamsRequest(req.teamIds.toString()),
                { response -> retrieveGame(req, RetrieveGameRequest(req.gameId), response, done, fail) },
                { err -> fail(err) }
        )
    }

    private fun retrieveGame(playRequest: Play01Request, gameRequest: RetrieveGameRequest, teamResponse: RetrieveTeamsResponse, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveGameUsecase.start(gameRequest,
                { response -> retrieveTurns(playRequest, response, teamResponse, done, fail) },
                { err -> fail(err) }
        )
    }

    private fun retrieveTurns(playRequest: Play01Request, gameResponse: RetrieveGameResponse, teamResponse: RetrieveTeamsResponse, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTurnsUsecase.exec(RetrieveTurnsRequest(gameResponse.game.id),
                { response ->
                    gameResponse.game.start(playRequest.create.startIndex, teamResponse.teams)
                    response.turns.forEach { gameResponse.game.handle(it) }
                    done.invoke(Play01Response(gameResponse.game, teamResponse.teams))
                }, { err -> fail(err) })
    }
}