package nl.entreco.domain.beta

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.FeatureRepository
import javax.inject.Inject

/**
 * Created by entreco on 03/02/2018.
 */
class FetchFeaturesUsecase @Inject constructor(private val repo: FeatureRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg){

    fun exec(req: FetchFeaturesRequest, done: (FetchFeaturesResponse)->Unit, fail: (Throwable)-> Unit){
        onBackground({

            val features = repo.fetchAll()
            onUi { done(FetchFeaturesResponse(features)) }
        }, fail)
    }
}