package nl.entreco.dartsscorecard.play

import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.play.stream.StreamController
import nl.entreco.dartsscorecard.play.stream.StreamFragment
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import nl.entreco.domain.model.players.Team
import javax.inject.Inject

/**
 * Created by entreco on 24/02/2018.
 */
class Play01Navigator @Inject constructor(private val activity: Play01Activity) {

    private val fm : FragmentManager = activity.supportFragmentManager

    fun gotoTeamProfile(view: View, team: Team) {
        val teams = team.players.map { it.id }.filter { it > 0 }.toLongArray()
        if (teams.isEmpty()) {
            Snackbar.make(view, R.string.err_player_was_deleted, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { }
                    .show()
        } else {
            ProfileActivity.launch(activity, view, teams)
        }
    }

    fun attachVideoStream() {
        fm.beginTransaction()
                .add(R.id.streamContainer, StreamFragment(), StreamFragment.TAG)
                .commit()
    }

    fun detachVideoStream() {
        val streamFragment = fm.findFragmentById(R.id.streamContainer)
        fm.beginTransaction()
                .remove(streamFragment)
                .commit()
    }

    fun streamController(): StreamController? {
        return fm.findFragmentById(R.id.streamContainer) as? StreamController
    }
}
