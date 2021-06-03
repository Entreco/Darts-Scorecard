package nl.entreco.domain.profile.fetch

import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.domain.repository.ProfileStatRepository
import javax.inject.Inject


class FetchProfileStatsUsecase @Inject constructor(
    private val profileStatRepository: ProfileStatRepository,
    bg: nl.entreco.libcore.threading.Background, fg: nl.entreco.libcore.threading.Foreground
) : nl.entreco.libcore.BaseUsecase(bg, fg) {

    fun exec(req: FetchProfileStatRequest, done: (FetchProfileStatResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val stats = req.playerIds.map { playerId ->
                profileStatRepository.fetchForPlayer(playerId)
            }
            onUi { done(FetchProfileStatResponse(stats)) }
        }, fail)
    }
}