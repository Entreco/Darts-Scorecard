package nl.entreco.dartsscorecard.play.input

import android.os.SystemClock
import nl.entreco.dartsscorecard.analytics.Analytics
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Turn
import java.util.*
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
class InputViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), PlayerListener{

    var count = 0
    var dart1 : Int = -1
    var dart2 : Int = -1
    var dart3 : Int = -1

    fun submitRandom(listener: InputListener) {

        when {
            firstDart() -> {
                dart1 = rand()
                listener.onDartThrown(dart1)
            }
            secondDart() -> {
                dart2 = rand()
                listener.onDartThrown(dart2)
            }
            else -> {
                dart3 = rand()

                val turn = Turn(dart1, dart2, dart3)
                listener.onDartThrown(dart3)
                listener.onTurnSubmitted(turn)

                analytics.trackAchievement("scored: $turn")
            }
        }
        count++
    }

    private fun secondDart() = count % 3 == 1

    private fun firstDart() = count % 3 == 0

    private fun rand(): Int = Random().nextInt(20) * (Random().nextInt(2) + 1)

    override fun onNext(next: Next) {
        // Update Toolbar / Preferences for Team (or Player)
    }
}