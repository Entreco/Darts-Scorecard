package nl.entreco.dartsscorecard.play.input

import nl.entreco.domain.play.model.Turn

/**
 * Created by Entreco on 19/11/2017.
 */
interface InputListener {
    fun onDartThrown(score: Int)
    fun onTurnSubmitted(turn: Turn)
}