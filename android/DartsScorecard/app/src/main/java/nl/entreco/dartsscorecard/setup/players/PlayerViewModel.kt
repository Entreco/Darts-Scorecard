package nl.entreco.dartsscorecard.setup.players

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import android.widget.AdapterView

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerViewModel(id: Long, teamIndex: Int, name: String = "Player $teamIndex") {
    val playerId = ObservableLong(id)
    val name = ObservableField<String>(name)
    val teamIndex = ObservableInt(teamIndex - 1)

    fun onTeamSelected(adapter: AdapterView<*>, index: Int) {
        val resolved = adapter.getItemAtPosition(index).toString().toInt()
        teamIndex.set(resolved)
    }
}