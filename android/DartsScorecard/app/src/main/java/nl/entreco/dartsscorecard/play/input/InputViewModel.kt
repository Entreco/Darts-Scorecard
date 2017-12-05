package nl.entreco.dartsscorecard.play.input

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.Analytics
import nl.entreco.domain.Logger
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
open class InputViewModel @Inject constructor(private val analytics: Analytics, private val logger: Logger) : BaseViewModel(), PlayerListener {

    val toggle = ObservableBoolean(false)
    val current = ObservableField<Player>(NoPlayer())
    val scoredTxt = ObservableField<String>("")
    val nextDescription = ObservableInt(R.string.empty)
    val darts = ObservableField<Turn>()

    private val estimator = ScoreEstimator()
    private var turn = Turn()
    private var nextUp: Next? = null

    fun entered(score: Int) {
        val oldValue = scoredTxt.get()
        if (oldValue.length < 3) {
            scoredTxt.set(oldValue.plus(score.toString()))
        }
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
            this.scoredTxt.set(turn.total().toString())
        } else {
            submit(estimatedTurn, listener)
        }

        clearScoreInput()
    }

    private fun parseScore(input: TextView): Int {
        return try {
            input.text.toString().toInt()
        } catch (err: Exception) {
            logger.w("Unable to parse text from Score Input: ${input.text}")
            0
        }
    }

    private fun submitDart(dart: Dart, listener: InputListener) {
        turn += dart
        darts.set(turn)
        listener.onDartThrown(turn.copy(), nextUp?.player!!)

        when {
            lastDart() -> {
                submit(turn.copy(), listener)
            }
        }
    }

    private fun submit(turn: Turn, listener: InputListener) {
        listener.onTurnSubmitted(turn.copy(), nextUp?.player!!)

        this.scoredTxt.set(turn.total().toString())
        this.analytics.trackAchievement("scored: $turn")
        this.darts.set(turn)
    }

    private fun clearScoreInput() {
        scoredTxt.set("")
    }

    private fun lastDart() = turn.dartsLeft() <= 0

    override fun onNext(next: Next) {
        clearScoreInput()
        nextUp = next
        nextDescription.set(descriptionFromNext(next))
        current.set(next.player)
        turn = Turn()
        darts.set(turn)
    }

    private fun descriptionFromNext(next: Next): Int {
        return when (next.state) {
            State.START -> R.string.game_on
            State.LEG -> R.string.to_throw_first
            State.SET -> R.string.to_throw_first
            State.MATCH -> R.string.game_shot_and_match
            else -> R.string.to_throw
        }
    }

    private fun gameIsFinished() = nextUp == null || nextUp?.state == State.MATCH
}