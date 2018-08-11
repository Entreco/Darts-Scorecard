package nl.entreco.domain.streaming.receive

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import java.util.*
import javax.inject.Inject

class RegisterReceiverUsecase @Inject constructor(
        private val signallingRepository: SignallingRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun go(request: RegisterReceiverRequest, done: (RegisterReceiverResponse) -> Unit,
           fail: (Throwable) -> Unit) {

        onBackground({

            signallingRepository.connect()
            signallingRepository.cleanDisconnectOrders(onDisconnectSuccess(request, done, fail),
                    onError("error disconnecting orders", fail))
        }, fail)

    }

    private fun onDisconnectSuccess(request: RegisterReceiverRequest,
                                    done: (RegisterReceiverResponse) -> Unit,
                                    fail: (Throwable) -> Unit): () -> Unit = {
        // done
        // Start UseCase that listens for Disconnects
        // signallingRepository.listenForDisconnectOrders(request.onDisconnect)
        val connectCode = (0..4).map { Random().nextInt(9) }.joinToString("") { it.toString() }
        signallingRepository.setReceiverOnline(
                connectCode,
                onFoundDevice(done),
                onError("error find online device", fail))
    }

    private fun onFoundDevice(done: (RegisterReceiverResponse) -> Unit): (String) -> Unit {
        return { code ->
            onUi { done(RegisterReceiverResponse(code)) }
        }
    }

    private fun onError(msg: String, fail: (Throwable) -> Unit): (Throwable) -> Unit = { err ->
        onUi { fail(IllegalArgumentException(msg)) }
    }
}