package nl.entreco.dartsscorecard.play.input

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import nl.entreco.domain.Analytics
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
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
    private var turn = Turn()
    private var nextUp : Next? = null

    fun entered(score: Int){
        scoredTxt.set(scoredTxt.get().plus(score.toString()))
    }

    fun back(){
        scoredTxt.set(scoredTxt.get().dropLast(1))
    }

    fun submitRandom(listener: InputListener) {

        if(nextUp == null || nextUp?.state == State.MATCH) return

        if(toggle.get()){
            submitSingles(listener)
        } else {
            submitAll(listener)
        }
    }

    private fun submitAll(listener: InputListener) {
        val turn = Turn(Dart.random(), Dart.random(), Dart.random())
        submit(turn, listener)
    }

    private fun submitSingles(listener: InputListener) {
        when {
            firstDart() -> {
                turn += Dart.random()
                scoredTxt.set(turn.total().toString())
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
            secondDart() -> {
                turn += Dart.random()
                scoredTxt.set(turn.total().toString())
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
            else -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
                submit(turn.copy(), listener)

                turn = Turn()
            }
        }
        count++
    }

    private fun submit(turn: Turn, listener: InputListener){
        scoredTxt.set(turn.total().toString())
        listener.onTurnSubmitted(turn.copy(), nextUp?.player!!)
        analytics.trackAchievement("scored: $turn")
    }

    private fun secondDart() = count % 3 == 1

    private fun firstDart() = count % 3 == 0

    override fun onNext(next: Next) {
        scoredTxt.set("")
        nextUp = next
        current.set(next.player)
    }
}