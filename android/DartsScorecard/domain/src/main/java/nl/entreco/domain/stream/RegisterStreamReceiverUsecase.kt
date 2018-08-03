package nl.entreco.domain.stream

import android.os.SystemClock
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class RegisterStreamReceiverUsecase @Inject constructor(
        private val signallingRepository: SignallingRepository,
        bg: Background, fg: Foreground) :
        BaseUsecase(bg, fg) {
    fun exec(done: (String) -> Unit, err: (Throwable) -> Unit) {
        onBackground({

            signallingRepository.register("UNIQUE ID")
            SystemClock.sleep(5000)

            // Do Something
            onUi { done("Yeah") }

        }, err)
    }
}