package nl.entreco.domain.streaming.p2p

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.IceRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class ConnectToPeerUsecase @Inject constructor(
        private val iceRepository: IceRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: ConnectToPeerRequest, done: (ConnectToPeerResponse) -> Unit,
           fail: (Throwable) -> Unit) {
        onBackground({

        }, fail)

    }

}