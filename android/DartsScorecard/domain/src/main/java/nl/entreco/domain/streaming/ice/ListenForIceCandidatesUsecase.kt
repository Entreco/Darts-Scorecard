package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.IceRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class ListenForIceCandidatesUsecase @Inject constructor(
        private val repository: IceRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: ListenForIceCandidatesRequest,
           done: (ListenForIceCandidatesResponse) -> Unit,
           fail: (Throwable) -> Unit) {

        onBackground({

            repository.listenForIceCandidates(request.uuid,
                    shouldAdd(done, true),
                    shouldAdd(done, false))

        }, fail)

    }

    private fun shouldAdd(
            done: (ListenForIceCandidatesResponse) -> Unit,
            add: Boolean): (DscIceCandidate) -> Unit =
            { done(ListenForIceCandidatesResponse(it, add)) }

}