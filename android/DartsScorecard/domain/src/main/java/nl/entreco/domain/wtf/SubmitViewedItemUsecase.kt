package nl.entreco.domain.wtf

import nl.entreco.domain.Analytics
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.WtfRepository
import javax.inject.Inject

class SubmitViewedItemUsecase @Inject constructor(private val repo: WtfRepository, private val analytics: Analytics, bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(request: SubmitViewedItemRequest) {
        onBackground({
            repo.viewedItem(request.item.docId)
            analytics.trackViewFaq(request.item)
        }, {})
    }
}