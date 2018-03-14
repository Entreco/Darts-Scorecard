package nl.entreco.domain.play.mastercaller

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.Logger
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.SoundRepository
import javax.inject.Inject

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCaller @Inject constructor(private val logger: Logger,
                                       private val soundRepository: SoundRepository,
                                       bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun play(request: MasterCallerRequest){
        onBackground({
            soundRepository.play(request.toSound())
        }, onEnqueueFailed(request))
    }

    private fun onEnqueueFailed(request: MasterCallerRequest): (Throwable) -> Unit {
        return {
            logger.e("Error enqueing sound $request")
        }
    }

    fun stop() {
        onBackground({ soundRepository.release()}, {})
    }
}