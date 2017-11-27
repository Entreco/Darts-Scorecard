package nl.entreco.dartsscorecard.play.input

import android.databinding.ObservableBoolean
import nl.entreco.domain.Analytics
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.State
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
open class InputViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), PlayerListener {

    val toggle = ObservableBoolean(false)
    var count = 0
    private var turn = Turn()
    private var nextUp : Next? = null

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
        listener.onTurnSubmitted(turn.copy(), nextUp?.player!!)
        analytics.trackAchievement("scored: $turn")
    }

    private fun submitSingles(listener: InputListener) {
        when {
            firstDart() -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
            secondDart() -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
            else -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
                listener.onTurnSubmitted(turn.copy(), nextUp?.player!!)
                analytics.trackAchievement("scored: $turn")

                turn = Turn()
            }
        }
        count++
    }



    private fun secondDart() = count % 3 == 1

    private fun firstDart() = count % 3 == 0

    override fun onNext(next: Next) {
        nextUp = next
    }
}