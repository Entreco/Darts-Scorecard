package nl.entreco.dartsscorecard.beta

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.beta.FetchFeaturesUsecase
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaViewModel @Inject constructor(private val fetchFeaturesUsecase: FetchFeaturesUsecase) : BaseViewModel() {

    val isRefreshing = ObservableBoolean(false)
    private val features: MutableLiveData<List<Feature>> = MutableLiveData()

    fun refresh(refreshing: Boolean) {
        isRefreshing.set(refreshing)
        fetchFeaturesUsecase.subscribe({
            Log.d("Features", "features: $it")
            features.value = it
            isRefreshing.set(false)
        }, {})
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<List<Feature>>) {
        features.observe(owner, observer)
        refresh(false)
    }

    fun unsubscribe(owner: LifecycleOwner) {
        features.removeObservers(owner)
        fetchFeaturesUsecase.unsubscribe()
    }
}