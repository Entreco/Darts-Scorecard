package nl.entreco.dartsscorecard.play.input

import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.Analytics
import nl.entreco.domain.Logger
import nl.entreco.domain.play.listeners.InputListener
import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.ScoreEstimator
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent
import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
open class InputViewModel @Inject constructor(private val analytics: Analytics, private val logger: Logger) : BaseViewModel(), PlayerListener, InputEventsListener {

    val toggle = ObservableBoolean(false)
    val current = ObservableField<Player>(NoPlayer())
    val scoredTxt = ObservableField<String>("")
    val nextDescription = ObservableInt(R.string.empty)
    val hintProvider = ObservableField<HintKeyProvider>(HintKeyProvider(toggle.get()))
    val special = ObservableField<SpecialEvent?>()

    private val estimator = ScoreEstimator()
    private var turn = Turn()
    private var nextUp: Next? = null

    init {
        toggle.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateKeyboardHints()
            }

        })
    }

    private fun updateKeyboardHints() {
        hintProvider.set(HintKeyProvider(toggle.get()))
    }

    override fun onNoScoreEvent(event: NoScoreEvent) {
        special.set(if(event.noScore) event else null)
    }

    override fun onBustEvent(event: BustEvent) {
        special.set(event)
    }

    fun entered(score: Int) {
        val oldValue = scoredTxt.get()
        if (oldValue.length < 3) {
            scoredTxt.set(oldValue.plus(score.toString()))
        }
    }

    fun enteredHint(hint: Int, listener: InputListener) : Boolean {
        val score = parseScore(hintProvider.get().getHint(hint))
        if(hint == -1) { // Bust
            submit(score, listener, false)
        } else {
            submit(score, listener)
        }
        return true
    }

    fun back() {
        scoredTxt.set(scoredTxt.get().dropLast(1))
    }

    fun tryToSubmit(input: TextView, listener: InputListener) {
        val scored = extractScore(input)
        submit(scored, listener)
    }

    private fun submit(scored: Int, listener: InputListener, singles: Boolean = toggle.get()){
        if (gameIsFinished()) return

        // Estimate Darts thrown
        val estimatedTurn = estimator.guess(scored, singles)
        if (singles) {
            submitDart(estimatedTurn.first(), listener)
            this.scoredTxt.set(turn.total().toString())
        } else {
            submit(estimatedTurn, listener)
        }

        clearScoreInput()
    }

    private fun extractScore(input: TextView): Int {
        return parseScore(input.text.toString())
    }

    private fun parseScore(score: String): Int {
        return try {
            score.toInt()
        } catch (err: Exception) {
            logger.w("Unable to parse text from Score: $score")
            0
        }
    }

    private fun submitDart(dart: Dart, listener: InputListener) {
        turn += dart
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
    }

    private fun clearScoreInput() {
        scoredTxt.set("")
    }

    private fun lastDart() = turn.dartsLeft() <= 0

    override fun onNext(next: Next) {
        clearScoreInput()
        nextUp = next
        toggle.set(false)
        nextDescription.set(descriptionFromNext(next))
        current.set(next.player)
        turn = Turn()
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