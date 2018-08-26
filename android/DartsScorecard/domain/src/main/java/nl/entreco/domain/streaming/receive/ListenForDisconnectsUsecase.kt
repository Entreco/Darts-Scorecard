package nl.entreco.domain.streaming.receive

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class ListenForDisconnectsUsecase @Inject constructor(
        private val logger: Logger,
        private val signallingRepository: SignallingRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(done: () -> Unit) {
        onBackground({
            signallingRepository.cleanDisconnectOrders({}, {})
            signallingRepository.listenForDisconnects {
                onUi(done)
            }
        }, onError())
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            logger.w("Listen for Disconnects error :$it")
        }
    }
}