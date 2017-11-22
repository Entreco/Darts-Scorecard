package nl.entreco.dartsscorecard.play

import android.support.annotation.VisibleForTesting
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.input.InputListener
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.usecase.CreateGameUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(val scoreViewModel: ScoreViewModel, val inputViewModel: InputViewModel, createGameUseCase: CreateGameUsecase) : BaseViewModel(), InputListener {

    // Lazy to keep state
    private val game: Game by lazy { createGameUseCase.start() }
    private val playerListeners = mutableListOf<PlayerListener>()
    private val scoreListeners = mutableListOf<ScoreListener>()

    init {
        addScoreListener(scoreViewModel)
        addPlayerListener(inputViewModel)
    }

    override fun onDartThrown(score: Int) {
        Log.d("NICE", "dart:$score")
        notifyScoreListeners(score, game.next)
    }

    override fun onTurnSubmitted(turn: Turn) {
        handleTurn(turn)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleTurn(turn: Turn) {
        game.handle(turn)

        val next = game.next

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

    private fun notifyScoreListeners(dart: Int, current: Next) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onDartThrown(dart, current) }
        }
    }

    private fun notifyPlayerListeners(next: Next) {
        synchronized(playerListeners) {
            playerListeners.forEach { it.onNext(next) }
        }
    }
}