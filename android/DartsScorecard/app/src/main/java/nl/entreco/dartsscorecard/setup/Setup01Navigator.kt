package nl.entreco.dartsscorecard.setup

import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.dartsscorecard.setup.edit.EditPlayerActivity
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.repository.RetrieveGameRequest

/**
 * Created by Entreco on 02/01/2018.
 */
class Setup01Navigator(private val activity: Setup01Activity) {

    fun onEditPlayerName(player: PlayerViewModel) {
        EditPlayerActivity.start(activity, player.name.get())
    }

    fun launch(req: RetrieveGameRequest) {
        Play01Activity.startGame(activity, req)
        activity.finish()
    }
}