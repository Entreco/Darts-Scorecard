package nl.entreco.dartsscorecard.hiscores

import androidx.databinding.ObservableBoolean
import nl.entreco.domain.hiscores.HiScoreItem
import nl.entreco.domain.hiscores.SortHiScoresRequest
import nl.entreco.domain.hiscores.SortHiScoresUsecase

class LoadingViewModel(private val sortHiScoresUsecase: SortHiScoresUsecase) {

    val isLoading = ObservableBoolean(false)

    fun showSorted(items: Map<String, HiScoreItem>, ready: (List<HiScoreItemModel>) -> Unit) {
        // Sort in the background
        isLoading.set(true)
        sortHiScoresUsecase.go(SortHiScoresRequest(items), { scores ->
            isLoading.set(false)
            val mapped = scores.map { HiScoreItemModel(it.name, it.display, it.pos) }
            ready(mapped)
        }, {
            isLoading.set(false)
        })
    }
}