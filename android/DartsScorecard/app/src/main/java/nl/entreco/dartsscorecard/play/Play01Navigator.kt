package nl.entreco.dartsscorecard.play

import android.view.View
import nl.entreco.dartsscorecard.profile.ProfileActivity
import nl.entreco.domain.model.players.Team
import javax.inject.Inject

/**
 * Created by entreco on 24/02/2018.
 */
class Play01Navigator @Inject constructor(private val activity: Play01Activity) {

    fun gotoTeamProfile(view: View, team: Team) {
        ProfileActivity.launch(activity, view, team)
    }
}
