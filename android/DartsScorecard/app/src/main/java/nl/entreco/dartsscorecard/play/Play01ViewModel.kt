package nl.entreco.dartsscorecard.play

import android.support.annotation.VisibleForTesting
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.input.InputListener
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.domain.play.usecase.GetFinishUsecase
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.usecase.CreateGameUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(val scoreViewModel: ScoreViewModel, val inputViewModel: InputViewModel, val getFinishUsecase: GetFinishUsecase, createGameUseCase: CreateGameUsecase) : BaseViewModel(), InputListener {

    // Lazy to keep state
    private val game: Game by lazy { createGameUseCase.start() }
    private val playerListeners = mutableListOf<PlayerListener>()
    private val scoreListeners = mutableListOf<ScoreListener>()

    init {
        addScoreListener(scoreViewModel)
        addPlayerListener(scoreViewModel)
        addPlayerListener(inputViewModel)
    }

    fun resume() {
        notifyNextPlayer(game.next)
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        Log.d("NICE", "scored:${turn.last()} by:$by")
        notifyDartThrown(turn, by)
    }

    override fun onTurnSubmitted(turn: Turn, by: Player) {
        Log.d("NICE", "turn:$turn by:$by")
        handleTurn(turn, by)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun handleTurn(turn: Turn, by: Player) {
        game.handle(turn)

        val next = game.next

        notifyScoreChanged(game.scores, by)
        notifyNextPlayer(next)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun addScoreListener(scoreListener: ScoreListener) {
        synchronized(scoreListeners) {
            if (!scoreListeners.contains(scoreListener)) {
                scoreListeners.add(scoreListener)
            }
        }
    }

    private fun addPlayerListener(playerListener: PlayerListener) {
        synchronized(playerListeners) {
            if (!playerListeners.contains(playerListener)) {
                playerListeners.add(playerListener)
            }
        }
    }

    private fun notifyScoreChanged(scores : Array<Score>, by: Player) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onScoreChange(scores, by) }
        }
    }

    private fun notifyDartThrown(turn: Turn, by: Player) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onDartThrown(turn, by) }
        }
    }

    private fun notifyNextPlayer(next: Next) {
        synchronized(playerListeners) {
            playerListeners.forEach { it.onNext(next) }
        }
    }
}