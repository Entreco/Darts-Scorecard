package nl.entreco.dartsscorecard.play

import android.databinding.ObservableField
import android.support.annotation.VisibleForTesting
import nl.entreco.dartsscorecard.analytics.Analytics
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.Score
import nl.entreco.domain.play.model.Turn
import java.util.*
import javax.inject.Inject

/**
 * Created by Entreco on 11/11/2017.
 */
class Play01ViewModel @Inject constructor(createGameUsecase: CreateGameUsecase, private val analytics: Analytics) {

    private var g : Game = createGameUsecase.start()
    private val summary: StringBuilder = StringBuilder(g.state).newline()
    val history: ObservableField<String> = ObservableField(summary.toString())
    val score: ObservableField<String> = ObservableField()

    fun submitRandom(){
        val turn = Turn(rand(), rand(), rand())
        handleTurn(turn)
        analytics.track("PlayViewModel1")
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleTurn(turn : Turn){
        g.handle(turn)

        score.set(format(g.scores))

        summary.insert(0, "\n")
        summary.insert(0, "throw:$turn")
        summary.insert(0, "\n\n")
        summary.insert(0, g.state)
        history.set(summary.toString())
    }

    @VisibleForTesting
    fun format(scores: Array<Score>): String {
        return StringBuilder().apply {
            scores.forEach {
                append(it).newline()
            }
        }.toString()
    }

    private fun rand(): Int = Random().nextInt(20) * Random().nextInt(3)

    private fun StringBuilder.newline() : StringBuilder {
        return append("\n")
    }
}