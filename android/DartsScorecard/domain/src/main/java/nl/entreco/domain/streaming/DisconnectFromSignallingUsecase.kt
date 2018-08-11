package nl.entreco.domain.streaming

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class DisconnectFromSignallingUsecase @Inject constructor(
        private val signallingRepository: SignallingRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg){

    fun go(){

    }
}