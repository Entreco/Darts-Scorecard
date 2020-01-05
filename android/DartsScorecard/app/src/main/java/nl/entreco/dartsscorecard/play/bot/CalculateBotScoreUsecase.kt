package nl.entreco.dartsscorecard.play.bot

import android.os.SystemClock
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.play.finish.GetFinishRequest
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.liblog.Logger
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject
import kotlin.random.Random

class CalculateBotScoreUsecase @Inject constructor(
        private val logger: Logger,
        bg: Background, fg: Foreground
) : BaseUsecase(bg, fg) {

    fun go(next: Next, done: (Turn) -> Unit) {
        onBackground({

            if (next.player is Bot && next.state != State.MATCH) {

                logger.i("Required score: ${next.requiredScore}")

                SystemClock.sleep(1)


                val aha = next.requiredScore.score


                val turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.BULL)

                onUi { done(turn) }
            }

        }, {})
    }

    private fun gaussian(goal: Int, mu: Int, sigma: Int) : Dart {
        java.util.Random().nextGaussian()
        return Dart.TRIPLE_20
    }
}