package nl.entreco.domain.stream

import nl.entreco.domain.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class RegisterStreamReceiverUsecase @Inject constructor(bg: Background, fg: Foreground) :
        BaseUsecase(bg, fg) {
    fun exec(done: (String) -> Unit, err: (Throwable) -> Unit) {
        onBackground({

            // Do Something
            onUi { done("Yeah") }

        }, err)
    }
}