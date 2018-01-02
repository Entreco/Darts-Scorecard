package nl.entreco.dartsscorecard.setup.players

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableInt
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerViewModel(teamIndex: Int, name: String = "Player ${teamIndex + 1}") {
    val name = ObservableField<String>(name)
    val teamIndex = ObservableInt(teamIndex)
    val teams = ObservableArrayList<Int>()
    private var updating = AtomicBoolean(false)

    init {
        onTeamsUpdated(teamIndex)
        updating.set(false)
    }

    fun onTeamSelected(index: Int) {
        if (!updating.get()) {
            teamIndex.set(index)
        } else {
            teamIndex.set(teamIndex.get())
        }
        updating.set(false)
    }

    fun onTeamsUpdated(itemCount: Int) {
        updating.set(true)
        teams.clear()
        teams += 1..itemCount
    }
}