package nl.entreco.domain.beta

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.FeatureRepository
import javax.inject.Inject

/**
 * Created by entreco on 03/02/2018.
 */
class FetchFeaturesUsecase @Inject constructor(private val repo: FeatureRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: FetchFeaturesRequest, done: (List<Feature>) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val response = repo.fetchAll { handle(it, done) }
            handle(response, done)

        }, fail)
    }

    private fun handle(response: List<Feature>, done: (List<Feature>) -> Unit) {
        val sort = response.sortedBy { it.votes / it.required }
        onUi { done(sort) }
    }
}