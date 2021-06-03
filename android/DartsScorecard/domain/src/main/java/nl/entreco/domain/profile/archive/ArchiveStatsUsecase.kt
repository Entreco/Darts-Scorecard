package nl.entreco.domain.profile.archive

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.ArchiveRepository
import javax.inject.Inject


class ArchiveStatsUsecase @Inject constructor(
    private val archiveRepository: ArchiveRepository,
    bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(request: ArchiveStatsRequest, done: (ArchiveStatsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val scheduled = archiveRepository.archive(request.gameId)
            onUi { done(ArchiveStatsResponse(scheduled)) }
        }, fail)
    }
}