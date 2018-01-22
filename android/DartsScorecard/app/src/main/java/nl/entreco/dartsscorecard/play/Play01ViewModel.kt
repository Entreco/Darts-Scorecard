package nl.entreco.dartsscorecard.play

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadedNotifier
import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.listeners.*
import nl.entreco.domain.play.start.MarkGameAsFinishedRequest
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Response
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.play.stats.StoreTurnRequest
import nl.entreco.domain.settings.ScoreSettings
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(private val playGameUsecase: Play01Usecase, private val gameListeners: Play01Listeners, private val logger: Logger) : BaseViewModel(), UiCallback, InputListener {

    private lateinit var game: Game

    fun load(request: Play01Request, load: GameLoadedNotifier<ScoreSettings>, vararg loaders: GameLoadedNotifier<Play01Response>) {
        playGameUsecase.loadGameAndStart(request,
                { response ->
                    this.game = response.game
                    load.onLoaded(response.teams, game.scores, response.settings, this)
                    loaders.forEach {
                        it.onLoaded(response.teams, game.scores, response, null)
                    }
                },
                { err -> logger.e("err: $err") })
    }

    fun registerListeners(scoreListener: ScoreListener, statListener: StatListener, specialEventListener: SpecialEventListener<*>, vararg playerListeners: PlayerListener) {
        gameListeners.registerListeners(scoreListener, statListener, specialEventListener, *playerListeners)
    }

    override fun onLetsPlayDarts(listeners: List<TeamScoreListener>) {
        this.gameListeners.onLetsPlayDarts(game, listeners)
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        this.gameListeners.onDartThrown(turn, by)
    }

    override fun onTurnSubmitted(turn: Turn, by: Player) {
        handleTurn(turn, by)
        storeTurn(turn, by)
    }

    private fun handleTurn(turn: Turn, by: Player) {
        game.handle(turn)

        val next = game.next
        val scores = game.scores

        handleGameFinished(next, game.id)
        notifyListeners(next, turn, by, scores)
    }

    private fun storeTurn(turn: Turn, by: Player) {
        val turnRequest = StoreTurnRequest(by.id, game.id, turn)
        val score = game.previousScore()
        val started = game.isNewMatchlegOrSet()
        val turnCounter = game.getTurnCount()
        val breakMade = game.wasBreakMade(by)
        val turnMeta = TurnMeta(by.id, turnCounter, score, started, breakMade)
        playGameUsecase.storeTurnAndMeta(turnRequest, turnMeta, { turnId, metaId ->
            gameListeners.onStatsUpdated(turnId, metaId)
        })
    }

    private fun handleGameFinished(next: Next, gameId: Long) {
        if (next.state == State.MATCH) {
            playGameUsecase.markGameAsFinished(MarkGameAsFinishedRequest(gameId))
        }
    }

    private fun notifyListeners(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        gameListeners.onTurnSubmitted(next, turn, by, scores)
    }
}