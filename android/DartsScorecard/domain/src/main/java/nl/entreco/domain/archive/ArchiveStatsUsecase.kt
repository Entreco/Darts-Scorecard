package nl.entreco.domain.archive

import android.os.SystemClock
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.ArchiveRepository
import javax.inject.Inject


class ArchiveStatsUsecase @Inject constructor(
        private val archiveRepository: ArchiveRepository,
        bg: Background, fg: Foreground)
    : BaseUsecase(bg, fg){

    fun exec(request: ArchiveStatsRequest, done: (ArchiveStatsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            SystemClock.sleep(10000)

            val scheduled = archiveRepository.archive(request.gameId)
            onUi { done(ArchiveStatsResponse(scheduled)) }
        }, fail)
    }
}