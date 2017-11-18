package nl.entreco.dartsscorecard.play

import android.databinding.ObservableField
import android.support.annotation.VisibleForTesting
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.input.InputListener
import nl.entreco.dartsscorecard.play.score.ScoreKeeper
import nl.entreco.domain.play.model.Game
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
    private val summary: StringBuilder by lazy { StringBuilder(game.state).newline() }

    var scoreKeeper : ScoreKeeper? = null

    // Fields for UI updates
    val history: ObservableField<String> = ObservableField(summary.toString())

    override fun onDartThrown(score: Int) {
        Log.d("NICE", "dart:$score")
    }

    override fun onTurnSubmitted(turn: Turn) {
        handleTurn(turn)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleTurn(turn: Turn) {
        game.handle(turn)

        scoreKeeper?.onScoreChanged(game.scores)

        summary.insert(0, "\n")
        summary.insert(0, "throw:$turn")
        summary.insert(0, "\n\n")
        summary.insert(0, game.state)
        history.set(summary.toString())
    }

    private fun StringBuilder.newline(): StringBuilder {
        return append("\n")
    }
}