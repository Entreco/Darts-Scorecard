package nl.entreco.dartsscorecard.setup.players

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.AdapterView

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerViewModel(teamIndex: Int, name: String = "Player $teamIndex") {
    val name = ObservableField<String>(name)
    val teamIndex = ObservableInt(teamIndex - 1)

    fun onTeamSelected(adapter: AdapterView<*>, index: Int) {
        val resolved = adapter.getItemAtPosition(index).toString().toInt()
        teamIndex.set(resolved)
    }
}