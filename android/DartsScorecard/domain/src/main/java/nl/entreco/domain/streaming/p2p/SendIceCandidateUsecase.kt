package nl.entreco.domain.streaming.p2p

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.IceRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class SendIceCandidateUsecase @Inject constructor(
        private val iceRepository: IceRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: SendIceCandidateRequest, done: (SendIceCandidateResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            iceRepository.send(request.candidate) { success ->
                if (success) {
                    onUi { done(SendIceCandidateResponse("done")) }
                } else {
                    onUi { fail(IllegalStateException("Unable to add IceCandiate")) }
                }
            }

        }, fail)

    }

}