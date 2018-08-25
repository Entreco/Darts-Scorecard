package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.IceRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class FetchIceServerUsecase @Inject constructor(
        private val iceRepository: IceRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(
           done: (FetchIceServerResponse) -> Unit,
           fail: (Throwable) -> Unit) {

        onBackground({
            iceRepository.fetchIceServers { servers ->
                onUi { done(FetchIceServerResponse(servers)) }
            }
        }, fail)

    }
}