package nl.entreco.domain.play.archive

import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import javax.inject.Inject


class ArchiveStatsUsecase @Inject constructor(bg: Background, fg: Foreground) : BaseUsecase(bg, fg){
    fun exec(request: ArchiveStatsRequest, done: (ArchiveStatsResponse) -> Unit, fail: (Throwable) -> Unit) {

    }
}