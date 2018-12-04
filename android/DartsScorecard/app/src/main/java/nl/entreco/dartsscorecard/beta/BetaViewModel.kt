package nl.entreco.dartsscorecard.beta

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.purchases.connect.SubscribeToFeaturesUsecase
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaViewModel @Inject constructor(private val subscribeToFeaturesUsecase: SubscribeToFeaturesUsecase) : BaseViewModel() {

    private val features: MutableLiveData<List<Feature>> = MutableLiveData()

    fun refresh() {
        subscribeToFeaturesUsecase.subscribe({
            features.value = it
        }, {})
    }

    fun subscribe(owner: LifecycleOwner, observer: Observer<List<Feature>>) {
        features.observe(owner, observer)
        refresh()
    }

    fun unsubscribe(owner: LifecycleOwner) {
        features.removeObservers(owner)
        features.value = emptyList()
        subscribeToFeaturesUsecase.unsubscribe()
    }
}
