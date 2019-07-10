package nl.entreco.domain.play.mastercaller

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.SoundRepository
import nl.entreco.liblog.Logger
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