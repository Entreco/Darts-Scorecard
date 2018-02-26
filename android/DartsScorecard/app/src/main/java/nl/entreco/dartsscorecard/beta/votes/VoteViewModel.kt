package nl.entreco.dartsscorecard.beta.votes

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.beta.BetaAnimator
import nl.entreco.dartsscorecard.beta.BetaModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.beta.vote.SubmitVoteRequest
import nl.entreco.domain.beta.vote.SubmitVoteResponse
import nl.entreco.domain.beta.vote.SubmitVoteUsecase
import javax.inject.Inject

/**
 * Created by entreco on 07/02/2018.
 */
class VoteViewModel @Inject constructor(private val submitVoteUsecase: SubmitVoteUsecase, private val analytics: Analytics) : BaseViewModel(), BetaAnimator.Toggler {

    val feature = ObservableField<BetaModel>()
    val didAlreadyVote = ObservableBoolean(false)
    val votes = mutableListOf<String>()

    override fun onFeatureSelected(feature: BetaModel) {
        this.feature.set(feature)
        this.didAlreadyVote.set(votes.contains(feature.feature.ref))
        this.analytics.trackAchievement("viewed Feature ${feature.title.get()}")
    }

    fun submitDonation(donation: Donation) {
        submitVote(donation.votes)
    }

    fun submitVote(amount: Int) {
        val betaModel = feature.get()
        val currentFeature = betaModel.feature
        if (allowedToVote(betaModel, currentFeature)) {
            feature.set(BetaModel(currentFeature.copy(votes = currentFeature.votes + amount)))
            votes.add(currentFeature.ref)
            analytics.trackViewFeature(currentFeature)
            submitVoteUsecase.exec(SubmitVoteRequest(betaModel.feature.ref, amount), onVoteSuccess(currentFeature), onVoteFailed(currentFeature))
        }
    }

    private fun allowedToVote(betaModel: BetaModel, currentFeature: Feature) =
            betaModel.votable.get() && !votes.contains(currentFeature.ref)

    private fun onVoteSuccess(voted: Feature): (SubmitVoteResponse) -> Unit {
        return {
            analytics.trackAchievement("voted Feature ${voted.title}")
            didAlreadyVote.set(true)
        }
    }

    private fun onVoteFailed(voted: Feature): (Throwable) -> Unit {
        return {
            feature.set(BetaModel(voted))
            votes.remove(voted.ref)
            didAlreadyVote.set(false)
        }
    }
}
