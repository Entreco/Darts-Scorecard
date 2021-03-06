package nl.entreco.domain.play.mastercaller

import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class ToggleMusicUsecase @Inject constructor(
        private val audioPrefRepository: AudioPrefRepository, bg: Background, fg: Foreground
) : BaseUsecase(bg, fg) {
    fun toggle() {
        onBackground({
            val toggled = !audioPrefRepository.isBackgroundMusicEnabled()
            audioPrefRepository.setBackgroundMusicEnabled(toggled)
        }, {})
    }
}