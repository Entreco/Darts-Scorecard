package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Player

/**
 * Created by Entreco on 19/11/2017.
 */
interface InputListener {
    fun onUndo()
    fun onDartThrown(turn: Turn, by: Player)
    fun onTurnSubmitted(turn: Turn, by: Player)
}