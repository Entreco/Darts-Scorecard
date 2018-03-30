package nl.entreco.dartsscorecard.setup.settings

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.widget.AdapterView
import android.widget.SeekBar
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.setup.game.CreateGameRequest
import nl.entreco.domain.setup.settings.FetchPreferredSettingsUsecase
import nl.entreco.domain.setup.settings.FetchSettingsResponse
import nl.entreco.domain.setup.settings.StorePreferredSettingsUsecase
import nl.entreco.domain.setup.settings.StoreSettingsRequest
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class SettingsViewModel @Inject constructor(fetchPrefs: FetchPreferredSettingsUsecase, private val storePrefs: StorePreferredSettingsUsecase) : BaseViewModel() {

    private val preferred = ObservableField<FetchSettingsResponse>(FetchSettingsResponse())

    init {
        fetchPrefs.exec { preferred.set(it) }
    }

    val startScoreIndex = ObservableInt(preferred.get()!!.score)
    val min = preferred.get()!!.min
    val max = preferred.get()!!.max
    val startScore = ObservableInt()
    val numSets = ObservableInt(preferred.get()!!.sets)
    val numLegs = ObservableInt(preferred.get()!!.legs)

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
            numSets.set(sets)
        }
    }

    fun onLegsProgressChanged(legs: Int) {
        if (legs in min..max) {
            numLegs.set(legs)
        }
    }

    fun setupRequest(): CreateGameRequest {
        storePrefs.exec(StoreSettingsRequest(numSets.get(), numLegs.get(), min, max, startScoreIndex.get()))
        return CreateGameRequest(startScore.get(), 0, numLegs.get() + 1, numSets.get() + 1)
    }
}