package nl.entreco.domain.rating

import nl.entreco.shared.BaseUsecase
import nl.entreco.domain.repository.GameRepository
import nl.entreco.domain.repository.RatingPrefRepository
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class AskForRatingUsecase @Inject constructor(
        private val ratingPrefRepository: RatingPrefRepository,
        private val gameRepository: GameRepository, bg: Background, fg: Foreground) :
        BaseUsecase(bg, fg) {

    fun go(done: (AskForRatingResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            var shouldRateApp = false
            if (ratingPrefRepository.shouldAskToRateApp()) {
                val finishedGames = gameRepository.countFinishedGames()
                shouldRateApp = finishedGames >= 5
            }
            onUi { done(AskForRatingResponse(shouldRateApp)) }
        }, fail)
    }
}