package nl.entreco.dartsscorecard.setup.players

import android.content.Intent

/**
 * Created by Entreco on 03/01/2018.
 */
interface PlayerEditor {
    fun onEditPlayer(position: Int, player: PlayerViewModel, otherPlayers: List<Long>, otherBots: List<Long>)
    fun onAddNewPlayer(index: Int, otherPlayers: List<Long>, otherBots: List<Long>)
    fun handleResult(requestCode: Int, resultCode: Int, data: Intent?, callback: Callback)

    interface Callback {
        fun onPlayerEdited(position: Int, teamIndex: Int, playerName: String, playerId: Long)
        fun onPlayerAdded(playerName: String, playerId: Long)
        fun onBotAdded(botName: String, botId: Long)
    }
}