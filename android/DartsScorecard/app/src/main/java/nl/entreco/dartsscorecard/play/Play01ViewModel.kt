package nl.entreco.dartsscorecard.play

import android.support.annotation.VisibleForTesting
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.input.InputListener
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.usecase.CreateGameUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(createGameUseCase: CreateGameUsecase) : BaseViewModel(), InputListener {

    // Lazy to keep state
    private val game: Game by lazy { createGameUseCase.start() }
    private val playerListeners = mutableListOf<PlayerListener>()
    private val scoreListeners = mutableListOf<ScoreListener>()

    override fun onDartThrown(score: Int) {
        Log.d("NICE", "dart:$score")
    }

    override fun onTurnSubmitted(turn: Turn) {
        handleTurn(turn)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleTurn(turn: Turn) {
        game.handle(turn)

        val next = game.next!!

        notifyScoreListeners(game.scores, next)
        notifyPlayerListeners(next)
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

    private fun notifyScoreListeners(scores : Array<Score>, next: Next) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onScoreChange(scores, next) }
        }
    }

    private fun notifyPlayerListeners(next: Next) {
        synchronized(playerListeners) {
            playerListeners.forEach { it.onNext(next) }
        }
    }
}