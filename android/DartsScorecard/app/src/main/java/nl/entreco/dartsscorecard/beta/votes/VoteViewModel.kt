package nl.entreco.dartsscorecard.beta.votes

import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.beta.BetaAnimator
import nl.entreco.dartsscorecard.beta.BetaModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.beta.SubmitVoteRequest
import nl.entreco.domain.beta.SubmitVoteResponse
import nl.entreco.domain.beta.SubmitVoteUsecase
import javax.inject.Inject

/**
 * Created by entreco on 07/02/2018.
 */
class VoteViewModel @Inject constructor(private val submitVoteUsecase: SubmitVoteUsecase, private val analytics: Analytics) : BaseViewModel(), BetaAnimator.Toggler {

    val feature = ObservableField<BetaModel>()

    override fun onFeatureSelected(feature: BetaModel) {
        this.feature.set(feature)
        this.analytics.trackAchievement("viewed Feature ${feature.title.get()}")
    }

    fun submitVote(amount: Int) {
        val oldFeature = feature.get().feature
        feature.set(BetaModel(oldFeature.copy(votes = oldFeature.votes + amount)))
        submitVoteUsecase.exec(SubmitVoteRequest(this.feature.get().feature.ref, amount), onVoteSuccess(oldFeature), onVoteFailed(oldFeature))
    }

    private fun onVoteSuccess(oldFeature: Feature): (SubmitVoteResponse) -> Unit {
        return {
            analytics.trackAchievement("voted Feature ${oldFeature.title}")
        }
    }

    private fun onVoteFailed(oldFeature: Feature): (Throwable) -> Unit {
        return {
            feature.set(BetaModel(oldFeature))
        }
    }
}