package nl.entreco.domain.streaming

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class DisconnectUsecase @Inject constructor(
        private val logger: Logger,
        private val signallingRepository: SignallingRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(done:()->Unit) {
        onBackground({

            signallingRepository.disconnect()
            signallingRepository.stopListenForDisconnects()
            onUi(done)

        }, onError())
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            logger.w(it.localizedMessage)
        }
    }
}