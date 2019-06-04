package nl.entreco.domain.wtf

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.WtfRepository
import javax.inject.Inject

class SubscribeToWtfsUsecase @Inject constructor(private val repo: WtfRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun subscribe(done: (List<WtfItem>) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val response = repo.subscribe { handle(it, done) }
            handle(response, done)

        }, fail)
    }

    fun unsubscribe() {
        repo.unsubscribe()
    }

    private fun handle(response: List<WtfItem>, done: (List<WtfItem>) -> Unit) {
        val sort = response.sortedByDescending { it.viewed }
        onUi { done(sort) }
    }
}