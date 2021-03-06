package nl.entreco.dartsscorecard.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import nl.entreco.dartsscorecard.R
import nl.entreco.shared.scopes.ActivityScope
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.RatingPrefRepository
import javax.inject.Inject


/**
 * Created by entreco on 20/02/2018.
 */
class DialogHelper @Inject constructor(
        private val builder: AlertDialog.Builder,
        @ActivityScope private val ratingPrefRepository: RatingPrefRepository
) {

    fun revanche(previousIndex: Int, teams: Array<Team>, select: (Int) -> Unit) {

        if (onlyOneTeam(teams)) {
            select(0)
        } else if (moreThanOneTeam(teams)) {
            var selectedIndex = previousIndex
            builder
                    .setTitle(R.string.select_starting_team)
                    .setSingleChoiceItems(teams.map { it.toString() }.toTypedArray(),
                            previousIndex) { _, which ->
                        selectedIndex = which
                    }
                    .setPositiveButton(android.R.string.ok) { dialog, _ ->
                        select(selectedIndex)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
        }
    }

    private fun onlyOneTeam(teams: Array<Team>) = teams.size == 1
    private fun moreThanOneTeam(teams: Array<Team>) = teams.size > 1

    fun showConfirmDeleteDialog(onConfirmed: () -> Unit) {
        builder
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_message)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    onConfirmed()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    fun showRatingDialog() {
        builder
                .setTitle(R.string.rating_title)
                .setMessage(R.string.rating_message)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    ratingPrefRepository.neverAskAgain()
                    tryLaunchMarket(builder.context)
                }
                .setNegativeButton(R.string.rating_not_now) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNeutralButton(R.string.rating_never_ask_again) { dialog, _ ->
                    dialog.dismiss()
                    ratingPrefRepository.neverAskAgain()
                }
                .create()
                .show()
    }

    private fun tryLaunchMarket(context: Context) {
        val clean = context.packageName.removeSuffix(".dev")
        val uri = Uri.parse("market://details?id=$clean")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://play.google.com/store/apps/details?id=$clean")))
        } catch (e: ActivityNotFoundException) {

        }
    }
}
