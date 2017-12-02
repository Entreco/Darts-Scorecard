package nl.entreco.dartsscorecard.play.input

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.widget.TextView
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.Analytics
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.ScoreEstimator
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
open class InputViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), PlayerListener {

    val toggle = ObservableBoolean(false)
    val current = ObservableField<Player>(NoPlayer())
    val scoredTxt = ObservableField<String>("")
    var count = 0
    private val estimator = ScoreEstimator()
    private var turn = Turn()
    private var nextUp: Next? = null

    fun entered(score: Int) {
        scoredTxt.set(scoredTxt.get().plus(score.toString()))
    }

    fun back() {
        scoredTxt.set(scoredTxt.get().dropLast(1))
    }

    fun tryToSubmit(input: TextView, listener: InputListener) {
        val scored = parseScore(input)
        if (gameIsFinished()) return

        // Estimate Darts thrown
        val estimatedTurn = estimator.guess(scored, toggle.get())
        if (toggle.get()) {
            submitDart(estimatedTurn.first(), listener)
        } else {
            submit(estimatedTurn, listener)
        }

        clearScoreInput()
    }

    private fun gameIsFinished() = nextUp == null || nextUp?.state == State.MATCH

    private fun parseScore(input: TextView): Int {
        return try {
            input.text.toString().toInt()
        } catch (err: Exception) {
            0
        }
    }

    private fun submitDart(dart: Dart, listener: InputListener) {
        when {
            lastDart() -> {
                turn += dart
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
                submit(turn.copy(), listener)
                turn = Turn()
            }
            else -> {
                turn += dart
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
        }
        count++
    }

    private fun submit(turn: Turn, listener: InputListener) {
        scoredTxt.set(turn.total().toString())
        listener.onTurnSubmitted(turn.copy(), nextUp?.player!!)
        analytics.trackAchievement("scored: $turn")
    }

    private fun lastDart() = count % 3 == 2

    override fun onNext(next: Next) {
        clearScoreInput()
        nextUp = next
        current.set(next.player)
    }

    private fun clearScoreInput() {
        scoredTxt.set("")
    }
}