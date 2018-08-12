package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.AnswersRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class SendAnswerUsecase @Inject constructor(
        private val respository: AnswersRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: SendAnswerRequest, fail: (Throwable) -> Unit) {
        onBackground({
            respository.create(request.recipientUuid, request.localSessionDescription)
        }, fail)

    }

}