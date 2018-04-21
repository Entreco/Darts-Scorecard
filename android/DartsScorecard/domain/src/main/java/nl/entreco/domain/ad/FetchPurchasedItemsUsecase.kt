package nl.entreco.domain.ad

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.BillingRepository
import javax.inject.Inject


class FetchPurchasedItemsUsecase @Inject constructor(private val billingRepository: BillingRepository, bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(done: (FetchPurchasedItemsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val items = billingRepository.fetchPurchasedItems()
            onUi {
                done(FetchPurchasedItemsResponse(items.isEmpty()))
            }
        }, fail)
    }
}