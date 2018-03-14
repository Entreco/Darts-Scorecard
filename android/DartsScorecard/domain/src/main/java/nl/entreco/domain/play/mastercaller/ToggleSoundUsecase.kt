package nl.entreco.domain.play.mastercaller

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.AudioPrefRepository
import javax.inject.Inject

/**
 * Created by entreco on 14/03/2018.
 */
class ToggleSoundUsecase @Inject constructor(private val audioPrefRepository: AudioPrefRepository,bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {
    fun toggle(){
        onBackground({
            val toggled = !audioPrefRepository.isMasterCallerEnabled()
            audioPrefRepository.setMasterCallerEnabled(toggled)
        }, {})
    }
}