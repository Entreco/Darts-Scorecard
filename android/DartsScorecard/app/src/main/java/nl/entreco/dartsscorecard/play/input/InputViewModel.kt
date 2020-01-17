package nl.entreco.dartsscorecard.play.input

import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Animator
import nl.entreco.dartsscorecard.play.bot.CalculateBotScoreUsecase
import nl.entreco.domain.Analytics
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.model.players.NoPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.play.ScoreEstimator
import nl.entreco.domain.play.listeners.InputListener
import nl.entreco.domain.play.listeners.PlayerListener
import nl.entreco.domain.play.listeners.events.BustEvent
import nl.entreco.domain.play.listeners.events.NoScoreEvent
import nl.entreco.domain.play.listeners.events.SpecialEvent
import nl.entreco.liblog.Logger
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
class InputViewModel @Inject constructor(
        private val analytics: Analytics,
        private val logger: Logger,
        private val botUsecase: CalculateBotScoreUsecase
) : BaseViewModel(), PlayerListener, InputEventsListener {

    val nextUp = ObservableField<Next>()
    val toggle = ObservableBoolean(false)
    val current = ObservableField<Player>(NoPlayer())
    val scoredTxt = ObservableField("")
    val nextDescription = ObservableInt(R.string.empty)
    val hintProvider = ObservableField(HintKeyProvider(toggle.get()))
    val special = ObservableField<SpecialEvent?>()
    val required = ObservableField<Score>()
    val finalTurn = ObservableField<Turn?>()
    val dartsLeft = ObservableInt()
    val resumeDescription = ObservableInt(R.string.game_on)

    private val estimator = ScoreEstimator()
    private var turn = Turn()

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
        special.set(if (event.noScore) event else null)
    }

    override fun onBustEvent(event: BustEvent) {
        special.set(event)
    }

    fun back() {
        scoredTxt.set(scoredTxt.get()!!.dropLast(1))
    }

    fun clear(): Boolean {
        scoredTxt.set("")
        return true
    }

    fun onResume(animator: Play01Animator, listener: InputListener) {
        animator.expand()
        if (resumeDescription.get() == R.string.game_shot_and_match) {
            listener.onRevanche()
        }
    }

    fun entered(score: Int) {
        val oldValue = scoredTxt.get()!!
        if (oldValue.length < 3) {
            scoredTxt.set(oldValue.plus(score.toString()))
        }
    }

    fun onPressedKey(key: Int, listener: InputListener): Boolean {
        val score = parseScore(hintProvider.get()!!.getHintForKey(key))
        if (key == -1) { // Bust
            submit(score, listener, false)
        } else {
            submit(score, listener)
        }
        return true
    }

    fun onSubmitScore(input: TextView, listener: InputListener) {
        val scored = extractScore(input)
        submit(scored, listener)
    }

    fun onUndoPressed(listener: InputListener) {
        listener.onUndo()
        clearScoreInput()
    }

    private fun submit(scored: Int, listener: InputListener, singles: Boolean = toggle.get()) {
        if (gameIsFinished()) return

        // Estimate Darts thrown
        val estimatedTurn = estimator.guess(scored, singles)
        if (singles) {
            submitDart(estimatedTurn.first(), listener)
            this.scoredTxt.set(turn.total().toString())
        } else {
            submitScore(estimatedTurn, listener)
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
            logger.w("Unable to fromTeams text from Score: $score")
            0
        }
    }

    private fun submitDart(dart: Dart, listener: InputListener) {
        logger.i("Target submit: $dart")
        turn += dart
        logger.i("Target submit: $turn")
        dartsLeft.set(turn.dartsLeft())
        listener.onDartThrown(turn.copy(), nextUp.get()?.player!!)

        when {
            lastDart()     -> submitScore(turn.copy(), listener)
            didFinishLeg() -> submitScore(turn.copy(), listener)
            didBust()      -> submitScore(turn.copy(), listener)
        }
    }

    private fun submitScore(turn: Turn, listener: InputListener) {
        askForDartsAtFinish(turn, required.get(), listener)
    }

    private fun askForDartsAtFinish(turn: Turn, score: Score?, listener: InputListener) {
        if (turn.total() == score?.score && turn.hasZeros() && !toggle.get()) {
            finalTurn.set(turn)
        } else {
            done(turn, listener)
        }
    }

    fun onFinishWith(dartsUsed: Int, listener: InputListener) {
        val turn = finalTurn.get()?.setDartsUsedForFinish(dartsUsed)!!
        done(turn, listener)
    }

    private fun done(turn: Turn, listener: InputListener) {
        listener.onTurnSubmitted(turn.copy(), nextUp.get()?.player!!)
        this.scoredTxt.set(turn.total().toString())
        this.analytics.trackScore("scored: $turn", turn.total())
        clearScoreInput()
    }

    private fun clearScoreInput() {
        scoredTxt.set("")
    }

    private fun lastDart() = turn.dartsLeft() <= 0

    override fun onNext(next: Next) {
        clearScoreInput()
        nextUp.set(next)
        toggle.set(false)
        required.set(next.requiredScore)
        nextDescription.set(descriptionFromNext(next))
        resumeDescription.set(resumeDescriptionFromNext(next))
        current.set(next.player)
        finalTurn.set(null)
        turn = Turn()
        dartsLeft.set(turn.dartsLeft())
    }

    fun goBotGo(handler: Handler, listener: InputListener) {
        nextUp.get()?.let { next ->
            botUsecase.go(next) { turn ->
                toggle.set(true)

                val delay = 350L

                // No Bust
                val d1 = turn.first()
                handler.postDelayed({
                    submitDart(d1, listener)
                }, delay * 1)

                val d2 = turn.second()
                if (d2 != Dart.NONE) {
                    handler.postDelayed({
                        submitDart(d2, listener)
                    }, delay * 2)
                }

                val d3 = turn.third()
                if (d3 != Dart.NONE) {
                    handler.postDelayed({
                        submitDart(d3, listener)
                    }, delay * 3)
                }
            }
        }
    }


    private fun descriptionFromNext(next: Next): Int {
        return when (next.state) {
            State.START -> R.string.game_on
            State.LEG   -> R.string.to_throw_first
            State.SET   -> R.string.to_throw_first
            State.MATCH -> R.string.game_shot_and_match
            else        -> R.string.to_throw
        }
    }

    private fun resumeDescriptionFromNext(next: Next): Int {
        return when (next.state) {
            State.START -> R.string.game_on
            State.LEG   -> R.string.tap_to_resume
            State.SET   -> R.string.tap_to_resume
            State.MATCH -> R.string.game_shot_and_match
            else        -> R.string.tap_to_resume
        }
    }

    private fun didBust() = required.get()!!.score - turn.total() <= 1
    private fun didFinishLeg() = required.get()!!.score == turn.total()
    private fun gameIsFinished() = nextUp.get() == null || nextUp.get()?.state == State.MATCH
}

object Binding {
    @JvmStatic
    @BindingAdapter("next", "vm", "listener")
    fun doBot(view: View, next: Next?, vm: InputViewModel, listener: InputListener) {
        view.visibility = if (next?.player is Bot) View.VISIBLE else View.GONE
        if (next?.player is Bot) vm.goBotGo(Handler(), listener)
    }
}
