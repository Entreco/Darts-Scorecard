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

    fun submitRandom(listener: InputListener) {
        val dart1 = rand()
        val dart2 = rand()
        val dart3 = rand()
        val turn = Turn(dart1, dart2, dart3)


        listener.onDartThrown(dart1)
        listener.onDartThrown(dart2)
        listener.onDartThrown(dart3)
        listener.onTurnSubmitted(turn)

        analytics.trackAchievement("scored: $turn")
    }

    private fun rand(): Int = Random().nextInt(20) * (Random().nextInt(2) + 1)

    override fun onNext(next: Next) {
        // Update Toolbar / Preferences for Team (or Player)
    }
}