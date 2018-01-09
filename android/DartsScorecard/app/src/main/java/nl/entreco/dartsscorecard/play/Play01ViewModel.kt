package nl.entreco.dartsscorecard.play

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.score.GameLoadable
import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.dartsscorecard.play.score.UiCallback
import nl.entreco.domain.Logger
import nl.entreco.domain.model.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.listeners.InputListener
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.start.MarkGameAsFinishedRequest
import nl.entreco.domain.play.start.Play01Request
import nl.entreco.domain.play.start.Play01Usecase
import nl.entreco.domain.play.stats.StoreTurnRequest
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(private val playGameUsecase: Play01Usecase, private val logger: Logger) : BaseViewModel(), UiCallback, InputListener {

    // Lazy to keep state
    private lateinit var game: Game
    private val playerListeners = mutableListOf<PlayerListener>()
    private val scoreListeners = mutableListOf<ScoreListener>()
    private val specialEventListeners = mutableListOf<SpecialEventListener<*>>()

    fun load(request: Play01Request, load: GameLoadable) {
        playGameUsecase.loadGameAndStart(request,
                { response ->
                    this.game = response.game
                    load.startWith(response.teams, game.scores, request.createRequest(), this)
                },
                { err -> logger.e("err: $err") })
    }

    override fun onLetsPlayDarts(listeners: List<TeamScoreListener>) {
        listeners.forEach { addSpecialEventListener(it) }
        notifyNextPlayer(game.next)
        notifyScoreChanged(game.scores, game.next.player)
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        notifyDartThrown(turn, by)
    }

    override fun onTurnSubmitted(turn: Turn, by: Player) {
        handleTurn(turn, by)
        storeTurn(turn)
    }

    private fun storeTurn(turn: Turn) {
        playGameUsecase.storeTurn(StoreTurnRequest(game.id, turn))
    }

    private fun handleTurn(turn: Turn, by: Player) {
        game.handle(turn)

        val next = game.next
        val scores = game.scores

        handleGameFinished(next, game.id)
        notifyAboutSpecialEvents(next, turn, by, scores)
        notifyScoreChanged(scores, by)
        notifyNextPlayer(next)
    }

    private fun handleGameFinished(next: Next, gameId: Long) {
        if (next.state == State.MATCH) {
            playGameUsecase.markGameAsFinished(MarkGameAsFinishedRequest(gameId))
        }
    }

    fun addScoreListener(scoreListener: ScoreListener) {
        synchronized(scoreListeners) {
            if (!scoreListeners.contains(scoreListener)) {
                scoreListeners.add(scoreListener)
            }
        }
    }

    fun addPlayerListener(playerListener: PlayerListener) {
        synchronized(playerListeners) {
            if (!playerListeners.contains(playerListener)) {
                playerListeners.add(playerListener)
            }
        }
    }

    fun addSpecialEventListener(specialEventListener: SpecialEventListener<*>) {
        synchronized(specialEventListener) {
            if (!specialEventListeners.contains(specialEventListener)) {
                specialEventListeners.add(specialEventListener)
            }
        }
    }

    private fun notifyScoreChanged(scores: Array<Score>, by: Player) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onScoreChange(scores, by) }
        }
    }

    private fun notifyDartThrown(turn: Turn, by: Player) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onDartThrown(turn, by) }
        }
    }

    private fun notifyAboutSpecialEvents(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        synchronized(specialEventListeners) {
            specialEventListeners.forEach { it.onSpecialEvent(next, turn, by, scores) }
        }
    }

    private fun notifyNextPlayer(next: Next) {
        synchronized(playerListeners) {
            playerListeners.forEach { it.onNext(next) }
        }
    }
}