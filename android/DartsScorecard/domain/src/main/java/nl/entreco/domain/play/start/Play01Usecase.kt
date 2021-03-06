package nl.entreco.domain.play.start

import nl.entreco.domain.model.TurnMeta
import nl.entreco.domain.play.stats.*
import nl.entreco.domain.settings.ScoreSettings
import nl.entreco.liblog.Logger
import javax.inject.Inject

/**
 * Created by Entreco on 17/12/2017.
 */
class Play01Usecase @Inject constructor(private val retrieveGameUsecase: RetrieveGameUsecase,
                                        private val retrieveTurnsUsecase: RetrieveTurnsUsecase,
                                        private val retrieveTeamsUsecase: RetrieveTeamsUsecase,
                                        private val storeTurnUsecase: StoreTurnUsecase,
                                        private val storeMetaUsecase: StoreMetaUsecase,
                                        private val undoTurnUsecase: UndoTurnUsecase,
                                        private val markGameAsFinishedUsecase: MarkGameAsFinishedUsecase,
                                        private val deleteGameUsecase: DeleteGameUsecase,
                                        private val logger: Logger) {

    fun loadGameAndStart(req: Play01Request, done: (Play01Response) -> Unit, fail: (Throwable) -> Unit) {
        retrieveTeams(req, done, fail)
    }

    fun storeTurnAndMeta(req: StoreTurnRequest, turnMeta: TurnMeta, done: (Long, Long) -> Unit) {
        storeTurnUsecase.exec(req,
                onStoreTurnSuccess(req.gameId, turnMeta, done),
                onFailed("Storing Turn failed ${req.turn}"))
    }

    fun undoLastTurn(req: UndoTurnRequest, done: (UndoTurnResponse) -> Unit, fail: (Throwable) -> Unit) {
        undoTurnUsecase.exec(req, done, fail)
    }

    fun markGameAsFinished(finishRequest: MarkGameAsFinishedRequest) {
        markGameAsFinishedUsecase.exec(finishRequest)
    }

    fun deleteMatch(deleteRequest: DeleteGameRequest, done: ()->Unit){
        deleteGameUsecase.exec(deleteRequest, done)
    }

    private fun onStoreTurnSuccess(gameId: Long, turnMeta: TurnMeta, done: (Long, Long) -> Unit) = { response: StoreTurnResponse ->
        val metaRequest = StoreMetaRequest(response.turnId, gameId, response.turn, turnMeta)
        storeMetaUsecase.exec(metaRequest, done, onFailed("Storing Stat failed $turnMeta"))
    }

    private fun onFailed(msg: String) = { err: Throwable ->
        logger.w("$msg (${err.localizedMessage})")
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

                    /**
                     *  <b>This 'prepares' the game for resuming.</b>
                     *  It makes sure all Scores are correctly applied,
                     *  but also causes Play01ViewModel to have to ask Game
                     *  for properties related to Stats only
                     *  <b>PRO</b> -> Ensures an up-to-date game is ready to be resumed
                     *  <b>CON</b> -> Game is responsible for tracking Stats - related - stuff
                     */
                    response.turns.forEach { gameResponse.game.handle(it.second) }

                    val scoreSettings = ScoreSettings(playRequest.startScore, playRequest.numLegs, playRequest.numSets, playRequest.startIndex)
                    done.invoke(Play01Response(gameResponse.game, scoreSettings, teamResponse.teams, playRequest.teamIds))
                }, { err -> fail(err) })
    }
}
