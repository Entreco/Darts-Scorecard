package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.IceRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class CreateOfferUsecase @Inject constructor(
        private val respository: IceRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: CreateOfferRequest, done: (CreateOfferResponse) -> Unit,
           fail: (Throwable) -> Unit) {
        onBackground({

        }, fail)

    }

}