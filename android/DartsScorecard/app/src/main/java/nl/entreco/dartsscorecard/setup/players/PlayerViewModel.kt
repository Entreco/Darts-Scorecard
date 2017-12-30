package nl.entreco.dartsscorecard.setup.players

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.AdapterView

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerViewModel(number: Int, teamIndex: Int) {
    val name = ObservableField<String>("Player $number")
    val teamIndex = ObservableInt(teamIndex)
    val teams = ObservableArrayList<Int>()
    @Volatile var updating = false

    init {
        updateTeams(teamIndex)
    }

    fun onTeamSelected(adapter: AdapterView<*>, index: Int) {
        if(!updating) {
            teamIndex.set(index)
        } else {
            updating = false
            teamIndex.set(teamIndex.get())
        }
    }

    fun updateTeams(itemCount: Int) {
        updating = true
        teams.clear()
        teams += 1..itemCount
    }
}