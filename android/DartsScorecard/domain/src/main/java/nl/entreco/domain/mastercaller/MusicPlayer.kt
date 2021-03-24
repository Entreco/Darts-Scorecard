package nl.entreco.domain.mastercaller

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class MusicPlayer @Inject constructor(
        private val musicRepository: MusicRepository,
        bg: Background,
        fg: Foreground
) : BaseUsecase(bg, fg) {

    fun play() {
        onBackground({ musicRepository.play() }, {})
    }

    fun pause() {
        onBackground({ musicRepository.pause() }, {})
    }

    fun resume() {
        onBackground({ musicRepository.resume() }, {})
    }

    fun stop() {
        onBackground({ musicRepository.stop() }, {})
    }
}