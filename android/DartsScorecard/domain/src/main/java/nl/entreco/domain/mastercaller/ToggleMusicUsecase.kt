package nl.entreco.domain.mastercaller

import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject

class ToggleMusicUsecase @Inject constructor(
    private val audioPrefRepository: AudioPrefRepository,
    bg: Background,
    fg: Foreground,
) : BaseUsecase(bg, fg) {

    fun toggle() {
        onBackground({
            val toggled = !audioPrefRepository.isBackgroundMusicEnabled()
            audioPrefRepository.setBackgroundMusicEnabled(toggled)
        }, {})
    }
}