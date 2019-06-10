package nl.entreco.dartsscorecard.play

import com.google.android.material.snackbar.Snackbar
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import nl.entreco.domain.model.players.Team
import javax.inject.Inject

/**
 * Created by entreco on 24/02/2018.
 */
class Play01Navigator @Inject constructor(private val activity: Play01Activity) {

    fun gotoTeamProfile(view: View, team: Team) {
        val teams = team.players.map { it.id }.filter { it > 0 }.toLongArray()
        if (teams.isEmpty()) {
            Snackbar.make(view, R.string.err_player_was_deleted, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { }
                    .show()
        } else {
            ProfileActivity.launch(activity, teams, view)
        }
    }
}
