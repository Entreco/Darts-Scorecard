package nl.entreco.domain.wtf

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.WtfRepository
import javax.inject.Inject

class SubmitViewedItemUsecase @Inject constructor(private val repo: WtfRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(request: SubmitViewedItemRequest) {
        onBackground({
            repo.viewedItem(request.docId)
        }, {})
    }
}