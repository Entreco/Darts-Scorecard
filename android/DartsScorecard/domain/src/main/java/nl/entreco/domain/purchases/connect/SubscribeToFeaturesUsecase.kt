package nl.entreco.domain.purchases.connect

import nl.entreco.domain.beta.Feature
import nl.entreco.domain.repository.FeatureRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

/**
 * Created by entreco on 03/02/2018.
 */
class SubscribeToFeaturesUsecase @Inject constructor(
    private val repo: FeatureRepository, bg: Background, fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun subscribe(done: (List<Feature>) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val response = repo.subscribe { handle(it, done) }
            handle(response, done)

        }, fail)
    }

    fun unsubscribe() {
        repo.unsubscribe()
    }

    private fun handle(response: List<Feature>, done: (List<Feature>) -> Unit) {
        val sort = response.sortedBy { 1.0 - (it.votes.toDouble() / (it.required.toDouble() + 0.0000001)) }
        onUi { done(sort) }
    }
}