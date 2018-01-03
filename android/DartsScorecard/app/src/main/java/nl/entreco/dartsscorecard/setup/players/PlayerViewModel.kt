package nl.entreco.dartsscorecard.setup.players

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.widget.AdapterView
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Entreco on 30/12/2017.
 */
class PlayerViewModel(teamIndex: Int, name: String = "Player $teamIndex") {
    val name = ObservableField<String>(name)
    val teamIndex = ObservableInt(teamIndex)
    private var needToSkipInitialBecauseCalledAutomatically = AtomicBoolean(true)

    fun onTeamSelected(adapter: AdapterView<*>, index: Int) {
        if (needToSkipInitialBecauseCalledAutomatically.get()) {
            needToSkipInitialBecauseCalledAutomatically.set(false)
        } else {
            teamIndex.set(adapter.getItemAtPosition(index).toString().toInt())
        }
    }
}