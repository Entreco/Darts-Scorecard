package nl.entreco.dartsscorecard.base

import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.R.menu.edit
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
                    .setSingleChoiceItems(teams.map { it.toString() }.toTypedArray(), previousIndex) { _, which ->
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

    fun showStreamDialog(done: (String) -> Unit, cancel: ()->Unit) {
        val view = createCodeEditText()
        val et = view.findViewById<TextInputEditText>(R.id.enter)
        et.maxLines = 1
        et.inputType = InputType.TYPE_CLASS_NUMBER

        builder
                .setView(view)
                .setPositiveButton(R.string.live_stream_connect) { dialog, _ ->
                    done(et.text.toString())
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    cancel()
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    private fun createCodeEditText(): View {
        return LayoutInflater.from(builder.context).inflate(R.layout.dialog_connect_stream, null, false)
    }
}
