package nl.entreco.domain.beta.vote

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.FeatureRepository
import javax.inject.Inject

/**
 * Created by entreco on 07/02/2018.
 */
class SubmitVoteUsecase @Inject constructor(private val featureRepository: FeatureRepository, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground): nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(req: SubmitVoteRequest, done: (SubmitVoteResponse)->Unit, fail: (Throwable)->Unit){
        onBackground({

            featureRepository.submitVote(req.featureId, req.amount)
            onUi { done(SubmitVoteResponse(true)) }

        }, fail)
    }
}