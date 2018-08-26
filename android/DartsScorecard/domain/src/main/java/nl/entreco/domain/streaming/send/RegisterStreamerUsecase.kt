package nl.entreco.domain.streaming.send

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class RegisterStreamerUsecase @Inject constructor(
        private val signallingRepository: SignallingRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: RegisterStreamerRequest, done: (RegisterStreamerResponse) -> Unit,
           fail: (Throwable) -> Unit) {

        onBackground({

            signallingRepository.connect()
            signallingRepository.cleanDisconnectOrders(onDisconnectSuccess(request, done, fail), onError("error disconnecting orders", fail))
        }, onError("Unknown error", fail))

    }

    private fun onDisconnectSuccess(request: RegisterStreamerRequest,
                            done: (RegisterStreamerResponse) -> Unit,
                            fail: (Throwable) -> Unit): () -> Unit = {
            // done
            // Start UseCase that listens for Disconnects
            // signallingRepository.listenForDisconnectOrders(request.onDisconnect)
            signallingRepository.setStreamerOnline(request.deviceId,
                    onFoundDevice(done, fail),
                    onError("error find online device", fail))
    }

    private fun onFoundDevice(done: (RegisterStreamerResponse) -> Unit,
                      fail: (Throwable) -> Unit): (String?) -> Unit {
        return { uuid ->
            if (uuid != null) {
                // Bingo -> connect to the mofo
                onUi { done(RegisterStreamerResponse(uuid)) }
            } else {
                // Device Not Found
                onUi { fail(IllegalArgumentException("No device found")) }
            }
        }
    }

    private fun onError(msg: String, fail: (Throwable) -> Unit): (Throwable) -> Unit = { err ->
            onUi { fail(IllegalArgumentException(msg)) }
    }
}