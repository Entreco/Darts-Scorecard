package nl.entreco.dartsscorecard.beta

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.beta.FetchFeaturesRequest
import nl.entreco.domain.beta.FetchFeaturesUsecase
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaViewModel @Inject constructor(fetchFeaturesUsecase: FetchFeaturesUsecase) : BaseViewModel() {

    private val features: MutableLiveData<List<Feature>> = MutableLiveData()

    init {
        fetchFeaturesUsecase.exec(FetchFeaturesRequest(1), {
            Log.d("Features", "features: $it")
            features.value = it
        }, {})
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<List<Feature>>) {
        features.observe(owner, observer)
    }

    fun unsubscribe(owner: LifecycleOwner) {
        features.removeObservers(owner)
    }
}