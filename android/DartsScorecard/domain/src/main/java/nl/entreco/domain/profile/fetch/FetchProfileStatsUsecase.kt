package nl.entreco.domain.profile.fetch

import nl.entreco.domain.repository.ProfileStatRepository
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import javax.inject.Inject


class FetchProfileStatsUsecase @Inject constructor(
    private val profileStatRepository: ProfileStatRepository,
    bg: Background, fg: Foreground,
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