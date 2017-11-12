package nl.entreco.dartsscorecard

import android.databinding.ObservableField
import nl.entreco.domain.Game
import nl.entreco.domain.Turn
import java.util.*

/**
 * Created by Entreco on 11/11/2017.
 */
class SplashViewModel {

    private val g : Game = Game().apply { start() }
    private val summary: StringBuilder = StringBuilder(g.state).newline()
    val game : ObservableField<String> = ObservableField(summary.toString())

    fun submitRandom(){
        val turn = Turn(rand(), rand(), rand())
        g.handle(turn)
        summary.append(g.scores).newline().append(g.state).newline()
        game.set(summary.toString())
    }

    private fun rand(): Int = Random().nextInt(20) * Random().nextInt(3)

    fun StringBuilder.newline() : StringBuilder {
        return append("\n")
    }
}