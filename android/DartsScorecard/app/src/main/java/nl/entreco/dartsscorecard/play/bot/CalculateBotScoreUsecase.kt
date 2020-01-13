package nl.entreco.dartsscorecard.play.bot

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.liblog.Logger
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class CalculateBotScoreUsecase @Inject constructor(
        private val logger: Logger,
        private val finishUsecase: GetFinishUsecase,
        bg: Background, fg: Foreground
) : BaseUsecase(bg, fg) {

    private val aiming = mapOf(
            Dart.NONE to listOf(Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_5, Dart.TRIPLE_5),
            Dart.BULL to listOf(Dart.BULL, Dart.SINGLE_BULL),
            Dart.SINGLE_BULL to listOf(Dart.SINGLE_BULL, Dart.BULL),

            Dart.TRIPLE_20 to listOf(Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_5, Dart.TRIPLE_5),
            Dart.SINGLE_20 to listOf(Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_5, Dart.TRIPLE_5),
            Dart.DOUBLE_20 to listOf(Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_5, Dart.TRIPLE_5),

            Dart.TRIPLE_19 to listOf(Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_7, Dart.TRIPLE_7),
            Dart.SINGLE_19 to listOf(Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_7, Dart.TRIPLE_7),
            Dart.DOUBLE_19 to listOf(Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_7, Dart.TRIPLE_7),

            Dart.TRIPLE_18 to listOf(Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_4, Dart.TRIPLE_4),
            Dart.SINGLE_18 to listOf(Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_4, Dart.TRIPLE_4),
            Dart.DOUBLE_18 to listOf(Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_4, Dart.TRIPLE_4),

            Dart.TRIPLE_17 to listOf(Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_2, Dart.TRIPLE_2),
            Dart.SINGLE_17 to listOf(Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_2, Dart.TRIPLE_2),
            Dart.DOUBLE_17 to listOf(Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_2, Dart.TRIPLE_2),

            Dart.TRIPLE_16 to listOf(Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_7, Dart.TRIPLE_7),
            Dart.SINGLE_16 to listOf(Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_7, Dart.TRIPLE_7),
            Dart.DOUBLE_16 to listOf(Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_7, Dart.TRIPLE_7),

            Dart.TRIPLE_15 to listOf(Dart.SINGLE_15, Dart.TRIPLE_15, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_2, Dart.TRIPLE_2),
            Dart.SINGLE_15 to listOf(Dart.SINGLE_15, Dart.TRIPLE_15, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_2, Dart.TRIPLE_2),
            Dart.DOUBLE_15 to listOf(Dart.SINGLE_15, Dart.TRIPLE_15, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_2, Dart.TRIPLE_2),

            Dart.TRIPLE_14 to listOf(Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_9, Dart.TRIPLE_9),
            Dart.SINGLE_14 to listOf(Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_9, Dart.TRIPLE_9),
            Dart.DOUBLE_14 to listOf(Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_9, Dart.TRIPLE_9),

            Dart.TRIPLE_13 to listOf(Dart.SINGLE_13, Dart.TRIPLE_13, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_4, Dart.TRIPLE_4),
            Dart.SINGLE_13 to listOf(Dart.SINGLE_13, Dart.TRIPLE_13, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_4, Dart.TRIPLE_4),
            Dart.DOUBLE_13 to listOf(Dart.SINGLE_13, Dart.TRIPLE_13, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_4, Dart.TRIPLE_4),

            Dart.TRIPLE_12 to listOf(Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_5, Dart.TRIPLE_5),
            Dart.SINGLE_12 to listOf(Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_5, Dart.TRIPLE_5),
            Dart.DOUBLE_12 to listOf(Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_5, Dart.TRIPLE_5),

            Dart.TRIPLE_11 to listOf(Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_8, Dart.TRIPLE_8),
            Dart.SINGLE_11 to listOf(Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_8, Dart.TRIPLE_8),
            Dart.DOUBLE_11 to listOf(Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_8, Dart.TRIPLE_8),

            Dart.TRIPLE_10 to listOf(Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_15, Dart.TRIPLE_15),
            Dart.SINGLE_10 to listOf(Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_15, Dart.TRIPLE_15),
            Dart.DOUBLE_10 to listOf(Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_15, Dart.TRIPLE_15),

            Dart.TRIPLE_9 to listOf(Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_12, Dart.TRIPLE_12),
            Dart.SINGLE_9 to listOf(Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_12, Dart.TRIPLE_12),
            Dart.DOUBLE_9 to listOf(Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_12, Dart.TRIPLE_12),

            Dart.TRIPLE_8 to listOf(Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.TRIPLE_14),
            Dart.SINGLE_8 to listOf(Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.TRIPLE_14),
            Dart.DOUBLE_8 to listOf(Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.TRIPLE_14),

            Dart.TRIPLE_7 to listOf(Dart.SINGLE_7, Dart.TRIPLE_7, Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_19, Dart.TRIPLE_19),
            Dart.SINGLE_7 to listOf(Dart.SINGLE_7, Dart.TRIPLE_7, Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_19, Dart.TRIPLE_19),
            Dart.DOUBLE_7 to listOf(Dart.SINGLE_7, Dart.TRIPLE_7, Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_19, Dart.TRIPLE_19),

            Dart.TRIPLE_6 to listOf(Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_13, Dart.TRIPLE_13),
            Dart.SINGLE_6 to listOf(Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_13, Dart.TRIPLE_13),
            Dart.DOUBLE_6 to listOf(Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_13, Dart.TRIPLE_13),

            Dart.TRIPLE_5 to listOf(Dart.SINGLE_5, Dart.TRIPLE_5, Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_20, Dart.TRIPLE_20),
            Dart.SINGLE_5 to listOf(Dart.SINGLE_5, Dart.TRIPLE_5, Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_20, Dart.TRIPLE_20),
            Dart.DOUBLE_5 to listOf(Dart.SINGLE_5, Dart.TRIPLE_5, Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_20, Dart.TRIPLE_20),

            Dart.TRIPLE_4 to listOf(Dart.SINGLE_4, Dart.TRIPLE_4, Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_13, Dart.TRIPLE_13),
            Dart.SINGLE_4 to listOf(Dart.SINGLE_4, Dart.TRIPLE_4, Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_13, Dart.TRIPLE_13),
            Dart.DOUBLE_4 to listOf(Dart.SINGLE_4, Dart.TRIPLE_4, Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_13, Dart.TRIPLE_13),

            Dart.TRIPLE_3 to listOf(Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_17, Dart.TRIPLE_17),
            Dart.SINGLE_3 to listOf(Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_17, Dart.TRIPLE_17),
            Dart.DOUBLE_3 to listOf(Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_17, Dart.TRIPLE_17),

            Dart.TRIPLE_2 to listOf(Dart.SINGLE_2, Dart.TRIPLE_2, Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_15, Dart.TRIPLE_15),
            Dart.SINGLE_2 to listOf(Dart.SINGLE_2, Dart.TRIPLE_2, Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_15, Dart.TRIPLE_15),
            Dart.DOUBLE_2 to listOf(Dart.SINGLE_2, Dart.TRIPLE_2, Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_15, Dart.TRIPLE_15),

            Dart.TRIPLE_1 to listOf(Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_18, Dart.TRIPLE_18),
            Dart.SINGLE_1 to listOf(Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_18, Dart.TRIPLE_18),
            Dart.DOUBLE_1 to listOf(Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_18, Dart.TRIPLE_18)
    )


    fun go(next: Next, done: (Turn) -> Unit) {
        onBackground({
            if (next.player is Bot && next.state != State.MATCH) {
                logger.i("Required score: ${next.requiredScore}")
                val start = Turn()
                val finish1 = finishUsecase.calculateInBack(next.requiredScore.score, start.dartsLeft(), start.total(), -1)
                val target1 = Dart.fromString(finish1.split(" ").firstOrNull() ?: "T20")
                logger.i("Target 1: $target1")
                val dart1 = aiming[target1]?.random() ?: Dart.NONE
                logger.i("Target 1: $dart1")
                val turn1 = Turn(dart1)


                val finish2 = finishUsecase.calculateInBack(next.requiredScore.score, turn1.dartsLeft(), turn1.total(), -1)
                val target2 = Dart.fromString(finish2.split(" ").firstOrNull() ?: "T20")
                logger.i("Target 2: $target2")
                val dart2 = aiming[target2]?.random() ?: Dart.NONE
                logger.i("Target 2: $dart2")
                val turn2 = Turn(dart1, dart2)


                val finish3 = finishUsecase.calculateInBack(next.requiredScore.score, turn2.dartsLeft(), turn2.total(), -1)
                val target3 = Dart.fromString(finish3.split(" ").firstOrNull() ?: "T20")
                logger.i("Target 3: $target3")
                val dart3 = aiming[target3]?.random() ?: Dart.NONE
                logger.i("Target 3: $dart3")
                val turn = Turn(dart1, dart2, dart3)

                onUi { done(turn) }
            }
        }, {})
    }
}