package nl.entreco.dartsscorecard.play.input

import nl.entreco.dartsscorecard.analytics.Analytics
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Turn
import java.util.*
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
open class InputViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), PlayerListener {

    var count = 0
    private var turn = Turn()

    fun submitRandom(listener: InputListener) {

        when {
            firstDart() -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy())
            }
            secondDart() -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy())
            }
            else -> {
                turn += Dart.random()

                listener.onDartThrown(turn.copy())
                listener.onTurnSubmitted(turn.copy())
                analytics.trackAchievement("scored: $turn")

                turn = Turn()
            }
        }
        count++
    }

    private fun secondDart() = count % 3 == 1

    private fun firstDart() = count % 3 == 0

    override fun onNext(next: Next) {
        // Update Toolbar / Preferences for Team (or Player)
    }
}