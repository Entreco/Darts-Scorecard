package nl.entreco.dartsscorecard.setup.players

import android.content.Intent

/**
 * Created by Entreco on 03/01/2018.
 */
interface PlayerEditor {
    fun onEditPlayer(position: Int, player: PlayerViewModel)
    fun onAddNewPlayer(index: Int)
    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?, callback: PlayerEditor.Callback)

    interface Callback {
        fun onPlayerEdited(position: Int, teamIndex: Int, playerName: String)
        fun onPlayerAdded(playerName: String)
    }
}