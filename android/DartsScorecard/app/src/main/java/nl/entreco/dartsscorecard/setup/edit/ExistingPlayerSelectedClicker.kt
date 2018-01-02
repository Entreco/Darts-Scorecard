package nl.entreco.dartsscorecard.setup.edit

import android.view.View
import nl.entreco.domain.model.players.Player

/**
 * Created by Entreco on 02/01/2018.
 */
interface ExistingPlayerSelectedClicker {
    fun onSelected(view: View, player: Player)
}