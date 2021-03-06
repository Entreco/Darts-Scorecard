package nl.entreco.domain.beta.vote

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.FeatureRepository
import javax.inject.Inject

/**
 * Created by entreco on 07/02/2018.
 */
class SubmitVoteUsecase @Inject constructor(private val featureRepository: FeatureRepository, bg: Background, fg: Foreground): BaseUsecase(bg, fg) {

    fun exec(req: SubmitVoteRequest, done: (SubmitVoteResponse)->Unit, fail: (Throwable)->Unit){
        onBackground({

            featureRepository.submitVote(req.featureId, req.amount)
            onUi { done(SubmitVoteResponse(true)) }

        }, fail)
    }
}