package nl.entreco.dartsscorecard.setup.settings

import android.databinding.ObservableInt
import android.widget.AdapterView
import android.widget.SeekBar
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.repository.CreateGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class SettingsViewModel @Inject constructor() : BaseViewModel() {

    internal val startSets = 1
    internal val startLegs = 1
    val min = 0
    val max = 20
    val startScore = ObservableInt(501)
    val numSets = ObservableInt(startSets)
    val numLegs = ObservableInt(startLegs)

    fun onStartScoreSelected(adapter: AdapterView<*>, index: Int) {
        val selectedString = adapter.adapter.getItem(index) as? String
        startScore.set(selectedString?.toInt()!!)
    }

    fun onSetsChanged(seekBar: SeekBar, delta: Int) {
        seekBar.progress += delta
    }

    fun onLegsChanged(seekBar: SeekBar, delta: Int) {
        seekBar.progress += delta
    }

    fun onSetsProgressChanged(sets: Int) {
        if (sets in min..max) {
            numSets.set(sets + 1)
        }
    }

    fun onLegsProgressChanged(legs: Int) {
        if (legs in min..max) {
            numLegs.set(legs + 1)
        }
    }

    fun setupRequest(): CreateGameRequest {
        return CreateGameRequest(startScore.get(), 0, numLegs.get(), numSets.get())
    }
}