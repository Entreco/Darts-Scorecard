package nl.entreco.dartsscorecard.play

import android.support.annotation.VisibleForTesting
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.input.InputViewModel
import nl.entreco.dartsscorecard.play.score.ScoreViewModel
import nl.entreco.domain.play.listeners.InputListener
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.GetFinishUsecase
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(val scoreViewModel: ScoreViewModel, val inputViewModel: InputViewModel, val getFinishUsecase: GetFinishUsecase, createGameUseCase: CreateGameUsecase) : BaseViewModel(), InputListener {

    // Lazy to keep state
    private val game: Game by lazy { createGameUseCase.start() }
    private val playerListeners = mutableListOf<PlayerListener>()
    private val scoreListeners = mutableListOf<ScoreListener>()
    private val specialEventListeners = mutableListOf<SpecialEventListener<*>>()

    init {
        addScoreListener(scoreViewModel)
        addPlayerListener(scoreViewModel)
        addPlayerListener(inputViewModel)
        addSpecialEventListener(inputViewModel)
    }

    fun onReady() {
        notifyNextPlayer(game.next)
        notifyScoreChanged(game.scores, game.next.player)
    }

    override fun onDartThrown(turn: Turn, by: Player) {
        notifyDartThrown(turn, by)
    }

    override fun onTurnSubmitted(turn: Turn, by: Player) {
        handleTurn(turn, by)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
    fun handleTurn(turn: Turn, by: Player) {
        game.handle(turn)

        val next = game.next
        val scores = game.scores

        notifyAboutSpecialEvents(next, turn, by, scores)
        notifyScoreChanged(scores, by)
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


    private fun addSpecialEventListener(specialEventListener: SpecialEventListener<*>) {
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