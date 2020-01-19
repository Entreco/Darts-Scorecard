package nl.entreco.dartsscorecard.setup.edit

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import nl.entreco.dartsscorecard.setup.Setup01Navigator
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.model.players.Player

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerNavigator(private val activity: EditPlayerActivity) : ExistingPlayerSelectedClicker {
    override fun onSelected(player: Player) {
        activity.setResult(RESULT_OK, Setup01Navigator.editPlayerResponse(player, activity.intent))
        activity.finish()
    }

    override fun onSelected(bot: Bot) {
        activity.setResult(RESULT_OK, Setup01Navigator.editBotResponse(bot, activity.intent))
        activity.finish()
    }

    override fun onBackPressed() {
        activity.setResult(RESULT_CANCELED, Setup01Navigator.cancelPlayerResponse(activity.intent))
        activity.finish()
    }
}