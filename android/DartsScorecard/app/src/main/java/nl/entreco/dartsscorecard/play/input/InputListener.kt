package nl.entreco.dartsscorecard.play.input

import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.Player

/**
 * Created by Entreco on 19/11/2017.
 */
interface InputListener {
    fun onDartThrown(turn: Turn, by: Player)
    fun onTurnSubmitted(turn: Turn, by: Player)
}