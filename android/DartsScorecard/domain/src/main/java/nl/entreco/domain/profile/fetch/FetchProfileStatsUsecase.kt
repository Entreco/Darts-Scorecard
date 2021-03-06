package nl.entreco.domain.profile.fetch

import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import nl.entreco.domain.repository.ProfileStatRepository
import javax.inject.Inject


class FetchProfileStatsUsecase @Inject constructor(
        private val profileStatRepository: ProfileStatRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(req: FetchProfileStatRequest, done: (FetchProfileStatResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            val stats = req.playerIds.map { playerId ->
                profileStatRepository.fetchForPlayer(playerId)
            }
            onUi { done(FetchProfileStatResponse(stats)) }
        }, fail)
    }
}