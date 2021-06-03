package nl.entreco.domain.mastercaller

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.liblog.Logger
import javax.inject.Inject

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCaller @Inject constructor(
    private val logger: Logger,
    private val soundRepository: SoundRepository,
    bg: nl.entreco.libcore.threading.Background,
    fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun play(request: MasterCallerRequest) {
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
        onBackground({ soundRepository.release() }, {})
    }
}