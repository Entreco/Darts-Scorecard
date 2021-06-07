package nl.entreco.domain.wtf

import nl.entreco.domain.Analytics
import nl.entreco.domain.repository.WtfRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

class SubmitViewedItemUsecase @Inject constructor(
    private val repo: WtfRepository, private val analytics: Analytics, bg: Background, fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun exec(request: SubmitViewedItemRequest) {
        onBackground({
            repo.viewedItem(request.item.docId)
            analytics.trackViewFaq(request.item)
        }, {})
    }
}