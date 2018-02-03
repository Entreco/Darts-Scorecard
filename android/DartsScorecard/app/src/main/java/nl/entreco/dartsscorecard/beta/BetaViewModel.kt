package nl.entreco.dartsscorecard.beta

import android.util.Log
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.di.beta.BetaAdapter
import nl.entreco.domain.beta.FetchFeaturesRequest
import nl.entreco.domain.beta.FetchFeaturesUsecase
import javax.inject.Inject

/**
 * Created by entreco on 30/01/2018.
 */
class BetaViewModel @Inject constructor(private val fetchFeaturesUsecase: FetchFeaturesUsecase) : BaseViewModel() {

    fun fetch(adapter: BetaAdapter){
        fetchFeaturesUsecase.exec(FetchFeaturesRequest(1), {
            Log.d("Features", "features: ${it.features}")
            adapter.setFeatures(it.features)
        }, {

        })
    }
}