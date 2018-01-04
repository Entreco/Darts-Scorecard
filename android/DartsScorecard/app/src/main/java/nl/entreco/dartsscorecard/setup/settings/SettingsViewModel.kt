package nl.entreco.dartsscorecard.setup.settings

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.AdapterView
import android.widget.SeekBar
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.setup.usecase.FetchPreferredSettingsUsecase
import nl.entreco.domain.setup.usecase.FetchSettingsResponse
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class SettingsViewModel @Inject constructor(fetchPrefs: FetchPreferredSettingsUsecase) : BaseViewModel() {

    private val preferred = ObservableField<FetchSettingsResponse>(FetchSettingsResponse())

    init {
        fetchPrefs.exec { preferred.set(it) }
    }

    private val min = preferred.get().min
    val startScoreIndex = ObservableInt(0)
    val max = preferred.get().max
    val startScore = ObservableInt()
    val numSets = ObservableInt(preferred.get().startSets)
    val numLegs = ObservableInt(preferred.get().startLegs)

    fun onStartScoreSelected(adapter: AdapterView<*>, index: Int) {
        val resolved = adapter.getItemAtPosition(index).toString().toInt()
        startScoreIndex.set(index)
        startScore.set(resolved)
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