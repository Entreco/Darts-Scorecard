package nl.entreco.dartsscorecard.base

import android.support.v7.app.AlertDialog
import nl.entreco.dartsscorecard.R
import nl.entreco.domain.model.players.Team
import javax.inject.Inject

/**
 * Created by entreco on 20/02/2018.
 */
class DialogHelper @Inject constructor(private val builder: AlertDialog.Builder) {

    fun revanche(previousIndex: Int, teams: Array<Team>, select: (Int) -> Unit) {

        if (onlyOneTeam(teams)) {
            select(0)
        } else if (moreThanOneTeam(teams)) {
            var selectedIndex = previousIndex
            builder
                    .setTitle(R.string.select_starting_team)
                    .setSingleChoiceItems(teams.map { it.toString() }.toTypedArray(), previousIndex, { _, which ->
                        selectedIndex = which
                    })
                    .setPositiveButton(android.R.string.ok, { dialog, _ ->
                        select(selectedIndex)
                        dialog.dismiss()
                    })
                    .setNegativeButton(R.string.cancel, { dialog, _ ->
                        dialog.dismiss()
                    })
                    .create()
                    .show()
        }
    }

    private fun onlyOneTeam(teams: Array<Team>) = teams.size == 1
    private fun moreThanOneTeam(teams: Array<Team>) = teams.size > 1
}
