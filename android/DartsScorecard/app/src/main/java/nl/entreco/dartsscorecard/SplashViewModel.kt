package nl.entreco.dartsscorecard

import android.databinding.ObservableField
import nl.entreco.domain.Game
import nl.entreco.domain.GameSettings
import nl.entreco.domain.Score
import nl.entreco.domain.Turn
import java.util.*

/**
 * Created by Entreco on 11/11/2017.
 */
class SplashViewModel {

    private val g : Game = Game(GameSettings()).apply { start() }
    private val summary: StringBuilder = StringBuilder(g.state).newline()
    val history: ObservableField<String> = ObservableField(summary.toString())
    val score: ObservableField<String> = ObservableField()

    fun submitRandom(){
        val turn = Turn(rand(), rand(), rand())
        g.handle(turn)

        score.set(format(g.scores))

        summary.insert(0, "\n")
        summary.insert(0, "throw:$turn")
        summary.insert(0, "\n\n")
        summary.insert(0, g.state)
        history.set(summary.toString())
    }

    private fun format(scores: Array<Score>): String {
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