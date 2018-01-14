package nl.entreco.domain.play.start

import nl.entreco.domain.Logger
import nl.entreco.domain.model.Stats
import nl.entreco.domain.play.stats.*
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class Play01Usecase @Inject constructor(private val retrieveGameUsecase: RetrieveGameUsecase,
                                        private val retrieveTurnsUsecase: RetrieveTurnsUsecase,
                                        private val retrieveTeamsUsecase: RetrieveTeamsUsecase,
                                        private val storeTurnUsecase: StoreTurnUsecase,
                                        private val storeStatUsecase: StoreStatUsecase,
                                        private val markGameAsFinishedUsecase: MarkGameAsFinishedUsecase,
                                        private val logger: Logger) {

    fun loadGameAndStart(req: Play01Request, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeams(req, done, fail)
    }

    fun storeTurnAndStats(req: StoreTurnRequest, stats: Stats) {
        storeTurnUsecase.exec(req,
                onStoreTurnSuccess(req.playerId, req.gameId, stats),
                onFailed("Storing Turn failed ${req.turn}"))
    }

    private fun onStoreTurnSuccess(playerId: Long, gameId: Long, stats: Stats) = { response: StoreTurnResponse ->
        val statRequest = StoreStatRequest(playerId, response.statId, gameId, stats)
        storeStatUsecase.exec(statRequest, onFailed("Storing Stat failed $stats"))
    }

    private fun onFailed(msg: String) = { err: Throwable ->
        logger.w("$msg (${err.localizedMessage})")
    }

    fun markGameAsFinished(finishRequest: MarkGameAsFinishedRequest) {
        markGameAsFinishedUsecase.exec(finishRequest)
    }

    private fun retrieveTeams(req: Play01Request, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeamsUsecase.exec(RetrieveTeamsRequest(req.teamIds),
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
                    gameResponse.game.start(playRequest.startIndex, teamResponse.teams)
                    response.turns.forEach { gameResponse.game.handle(it.second) }
                    done.invoke(Play01Response(gameResponse.game, teamResponse.teams, response.turns))
                }, { err -> fail(err) })
    }
}