package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.OffersRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class ListenForOfferUsecase @Inject constructor(
        private val respository: OffersRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(done: (ListenForOfferResponse) -> Unit,
           fail: (Throwable) -> Unit) {
        onBackground({

            respository.listenForNewOffersWithUuid { change ->
                val response = ListenForOfferResponse(change.uuid, change.type, change.description)
                onUi { done(response) }
            }

        }, fail)

    }

}