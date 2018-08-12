package nl.entreco.domain.streaming.ice

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.AnswersRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class ListenForAnswersUsecase @Inject constructor(
        private val respository: AnswersRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(done: (ListenForAnswersResponse) -> Unit,
           fail: (Throwable) -> Unit) {
        onBackground({

            respository.listenForNewAnswers { change ->
                val response = ListenForAnswersResponse(change.uuid, change.type,
                        change.description)
                onUi { done(response) }
            }

        }, fail)

    }

}