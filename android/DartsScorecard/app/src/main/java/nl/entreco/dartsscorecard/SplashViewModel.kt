package nl.entreco.dartsscorecard

import android.databinding.ObservableField
import nl.entreco.domain.Game
import nl.entreco.domain.Turn

/**
 * Created by Entreco on 11/11/2017.
 */
class SplashViewModel {

    private val g : Game = Game().apply { start() }
    private val summary: StringBuilder = StringBuilder(g.state).newline()
    val game : ObservableField<String> = ObservableField(summary.toString())

    fun submitRandom(){
        val turn = Turn(20, 20, 20)
        g.handle(turn)
        summary.append(g.score).newline().append(g.state).newline()
        game.set(summary.toString())
    }

    fun StringBuilder.newline() : StringBuilder {
        return append("\n")
    }
}