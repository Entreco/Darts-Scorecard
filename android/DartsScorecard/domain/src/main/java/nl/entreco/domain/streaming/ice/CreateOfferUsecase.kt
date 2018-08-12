package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.IceRepository
import nl.entreco.domain.repository.OffersRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class CreateOfferUsecase @Inject constructor(
        private val respository: OffersRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: CreateOfferRequest, done: (CreateOfferResponse) -> Unit,
           fail: (Throwable) -> Unit) {
        onBackground({
            respository.create(request.description)
        }, fail)

    }

}