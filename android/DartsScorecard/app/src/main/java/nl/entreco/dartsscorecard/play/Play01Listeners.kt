package nl.entreco.dartsscorecard.play

import nl.entreco.dartsscorecard.play.score.TeamScoreListener
import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.ScoreListener
import nl.entreco.domain.play.listeners.SpecialEventListener
import nl.entreco.domain.play.listeners.StatListener
import javax.inject.Inject

/**
 * Created by entreco on 12/01/2018.
 */
class Play01Listeners @Inject constructor() {
    internal val scoreListeners = mutableListOf<ScoreListener>()
    internal val statListeners = mutableListOf<StatListener>()
    internal val playerListeners = mutableListOf<PlayerListener>()
    internal val specialEventListeners = mutableListOf<SpecialEventListener<*>>()

    fun registerListeners(scoreListener: ScoreListener, statListener: StatListener, specialEventListener: SpecialEventListener<*>, vararg playerListeners: PlayerListener) {
        addScoreListener(scoreListener)
        addStatListener(statListener)
        addSpecialEventListener(specialEventListener)
        playerListeners.forEach {
            addPlayerListener(it)
        }
    }

    fun onLetsPlayDarts(game: Game, listeners: List<TeamScoreListener>) {
        listeners.forEach { addSpecialEventListener(it) }
        notifyNextPlayer(game.next)
        notifyScoreChanged(game.scores, game.next.player)
    }

    fun onDartThrown(turn: Turn, by: Player) {
        synchronized(scoreListeners) {
            scoreListeners.forEach { it.onDartThrown(turn, by) }
        }
    }

    fun onTurnSubmitted(next: Next, turn: Turn, by: Player, scores: Array<Score>) {
        notifyAboutSpecialEvents(next, turn, by, scores)
        notifyScoreChanged(scores, by)
        notifyNextPlayer(next)
    }

    private fun addScoreListener(scoreListener: ScoreListener) {
        synchronized(scoreListeners) {
            if (!scoreListeners.contains(scoreListener)) {
                scoreListeners.add(scoreListener)
            }
        }
    }
    private fun addStatListener(statListener: StatListener) {
        synchronized(statListeners) {
            if (!statListeners.contains(statListener)) {
                statListeners.add(statListener)
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

    fun onStatsUpdated(turnId: Long, metaId: Long) {
        synchronized(statListeners){
            statListeners.forEach { it.onStatsChange(turnId, metaId) }
        }
    }
}