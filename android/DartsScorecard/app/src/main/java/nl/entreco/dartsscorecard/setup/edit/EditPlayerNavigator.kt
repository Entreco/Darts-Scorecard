package nl.entreco.dartsscorecard.setup.edit

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import nl.entreco.domain.model.players.Player

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerNavigator(private val activity: EditPlayerActivity) : ExistingPlayerSelectedClicker {
    override fun onSelected(view: View, player: Player) {
        val intent = Intent()
        intent.putExtra("oldName", activity.intent.getStringExtra("suggestion"))
        intent.putExtra("playerName", player.name)
        intent.putExtra("playerId", player.id)
        intent.putExtra("teamIndex", activity.intent.getIntExtra("teamIndex", -1))
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }
}