package nl.entreco.dartsscorecard.beta.votes

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
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
import kotlin.math.min

/**
 * Created by entreco on 07/02/2018.
 */
class VoteViewModel @Inject constructor(private val submitVoteUsecase: SubmitVoteUsecase, private val analytics: Analytics) : BaseViewModel(), BetaAnimator.Toggler {

    val feature = ObservableField<BetaModel>()
    val didAlreadyVote = ObservableBoolean(false)
    val votes = mutableListOf<String>()
    val showVideo = ObservableInt(View.GONE)

    override fun onFeatureSelected(feature: BetaModel) {
        this.feature.set(feature)
        this.showVideo.set(if (feature.video.get()!!.isNotBlank()) View.VISIBLE else View.GONE)
        this.didAlreadyVote.set(votes.contains(feature.feature.ref))
        this.analytics.trackAchievement("viewed Feature ${feature.title.get()}")
    }

    fun submitDonation(donation: Donation, done: ()->Unit) {
        submitVote(donation.votes, done)
    }

    fun launchVideo(view: View) {
        if (showVideo.get() == View.VISIBLE) {
            val uri = Uri.parse(feature.get()?.video?.get())
            val id = uri.getQueryParameter("v")
            val app = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
            val web = Intent(Intent.ACTION_VIEW, uri)

            try {
                view.context.startActivity(app)
            } catch (youtubeNotInstalled: ActivityNotFoundException) {
                view.context.startActivity(web)
            }
        }
    }

    fun submitVote(amount: Int) {
        submitVote(amount) {}
    }

    fun submitVote(amount: Int, done:()->Unit) {
        feature.get()?.let { betaModel ->
            val currentFeature = betaModel.feature
            if (allowedToVote(betaModel, currentFeature)) {
                feature.set(BetaModel(currentFeature.copy(votes = min(currentFeature.required, currentFeature.votes + amount))))
                votes.add(currentFeature.ref)
                analytics.trackViewFeature(currentFeature, amount)
                submitVoteUsecase.exec(SubmitVoteRequest(betaModel.feature.ref, amount), onVoteSuccess(currentFeature), onVoteFailed(currentFeature))
                done()
            }
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
