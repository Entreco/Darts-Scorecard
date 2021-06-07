package nl.entreco.dartsscorecard.play.bot

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Next
import nl.entreco.domain.model.State
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.play.finish.GetFinishUsecase
import nl.entreco.libcore.BaseUsecase
import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import nl.entreco.liblog.Logger
import java.util.*
import javax.inject.Inject
import kotlin.math.ceil

class CalculateBotScoreUsecase @Inject constructor(
    private val logger: Logger,
    private val finishUsecase: GetFinishUsecase,
    bg: Background, fg: Foreground
) : BaseUsecase(bg, fg) {

    private val aiming = mapOf(
            Dart.BULL to listOf(Dart.BULL, Dart.SINGLE_BULL, Dart.SINGLE_3, Dart.SINGLE_9, Dart.SINGLE_12, Dart.SINGLE_15, Dart.SINGLE_18, Dart.SINGLE_1, Dart.SINGLE_BULL, Dart.random(), Dart.random(), Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_BULL to listOf(Dart.SINGLE_BULL, Dart.BULL, Dart.SINGLE_2, Dart.SINGLE_8, Dart.SINGLE_11, Dart.SINGLE_14, Dart.SINGLE_17, Dart.SINGLE_20, Dart.SINGLE_BULL, Dart.random(), Dart.random(), Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),

            Dart.TRIPLE_20 to listOf(Dart.TRIPLE_20, Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_1, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_5, Dart.SINGLE_5, Dart.TRIPLE_5, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_20 to listOf(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_1, Dart.SINGLE_1, Dart.SINGLE_5, Dart.SINGLE_5, Dart.DOUBLE_20, Dart.TRIPLE_20),
            Dart.DOUBLE_20 to listOf(Dart.DOUBLE_20, Dart.ZERO, Dart.ZERO, Dart.SINGLE_20, Dart.SINGLE_1, Dart.SINGLE_5, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_19 to listOf(Dart.TRIPLE_19, Dart.SINGLE_19, Dart.SINGLE_19, Dart.SINGLE_3, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_7, Dart.SINGLE_7, Dart.TRIPLE_7, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_19 to listOf(Dart.SINGLE_19, Dart.SINGLE_19, Dart.SINGLE_3, Dart.SINGLE_3, Dart.SINGLE_7, Dart.SINGLE_7, Dart.DOUBLE_19, Dart.TRIPLE_19),
            Dart.DOUBLE_19 to listOf(Dart.DOUBLE_19, Dart.ZERO, Dart.ZERO, Dart.SINGLE_19, Dart.SINGLE_3, Dart.SINGLE_7, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_18 to listOf(Dart.TRIPLE_18, Dart.SINGLE_18, Dart.SINGLE_18, Dart.SINGLE_1, Dart.SINGLE_1, Dart.TRIPLE_1, Dart.SINGLE_4, Dart.SINGLE_4, Dart.TRIPLE_4, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_18 to listOf(Dart.SINGLE_18, Dart.SINGLE_18, Dart.SINGLE_1, Dart.SINGLE_1, Dart.SINGLE_4, Dart.SINGLE_4, Dart.DOUBLE_18, Dart.TRIPLE_18),
            Dart.DOUBLE_18 to listOf(Dart.DOUBLE_18, Dart.ZERO, Dart.ZERO, Dart.SINGLE_18, Dart.SINGLE_1, Dart.SINGLE_4, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_17 to listOf(Dart.TRIPLE_17, Dart.SINGLE_17, Dart.SINGLE_17, Dart.SINGLE_3, Dart.SINGLE_3, Dart.TRIPLE_3, Dart.SINGLE_2, Dart.SINGLE_2, Dart.TRIPLE_2, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_17 to listOf(Dart.SINGLE_17, Dart.SINGLE_17, Dart.SINGLE_3, Dart.SINGLE_3, Dart.SINGLE_2, Dart.SINGLE_2, Dart.DOUBLE_17, Dart.TRIPLE_17),
            Dart.DOUBLE_17 to listOf(Dart.DOUBLE_17, Dart.ZERO, Dart.ZERO, Dart.SINGLE_17, Dart.SINGLE_3, Dart.SINGLE_2, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_16 to listOf(Dart.TRIPLE_16, Dart.SINGLE_16, Dart.SINGLE_16, Dart.SINGLE_8, Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_7, Dart.SINGLE_7, Dart.TRIPLE_7, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_16 to listOf(Dart.SINGLE_16, Dart.SINGLE_16, Dart.SINGLE_8, Dart.SINGLE_8, Dart.SINGLE_7, Dart.SINGLE_7, Dart.DOUBLE_16, Dart.TRIPLE_16),
            Dart.DOUBLE_16 to listOf(Dart.DOUBLE_16, Dart.ZERO, Dart.ZERO, Dart.SINGLE_16, Dart.SINGLE_8, Dart.SINGLE_7, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_15 to listOf(Dart.TRIPLE_15, Dart.SINGLE_15, Dart.SINGLE_15, Dart.SINGLE_10, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_2, Dart.SINGLE_2, Dart.TRIPLE_2, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_15 to listOf(Dart.SINGLE_15, Dart.SINGLE_15, Dart.SINGLE_10, Dart.SINGLE_10, Dart.SINGLE_2, Dart.SINGLE_2, Dart.DOUBLE_15, Dart.TRIPLE_15),
            Dart.DOUBLE_15 to listOf(Dart.DOUBLE_15, Dart.ZERO, Dart.ZERO, Dart.SINGLE_15, Dart.SINGLE_10, Dart.SINGLE_2, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_14 to listOf(Dart.TRIPLE_14, Dart.SINGLE_14, Dart.SINGLE_14, Dart.SINGLE_11, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_9, Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_14 to listOf(Dart.SINGLE_14, Dart.SINGLE_14, Dart.SINGLE_11, Dart.SINGLE_11, Dart.SINGLE_9, Dart.SINGLE_9, Dart.DOUBLE_14, Dart.TRIPLE_14),
            Dart.DOUBLE_14 to listOf(Dart.DOUBLE_14, Dart.ZERO, Dart.ZERO, Dart.SINGLE_14, Dart.SINGLE_11, Dart.SINGLE_9, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_13 to listOf(Dart.TRIPLE_13, Dart.SINGLE_13, Dart.SINGLE_13, Dart.SINGLE_6, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_4, Dart.SINGLE_4, Dart.TRIPLE_4, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_13 to listOf(Dart.SINGLE_13, Dart.SINGLE_13, Dart.SINGLE_6, Dart.SINGLE_6, Dart.SINGLE_4, Dart.SINGLE_4, Dart.DOUBLE_13, Dart.TRIPLE_13),
            Dart.DOUBLE_13 to listOf(Dart.DOUBLE_13, Dart.ZERO, Dart.ZERO, Dart.SINGLE_13, Dart.SINGLE_6, Dart.SINGLE_4, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_12 to listOf(Dart.TRIPLE_12, Dart.SINGLE_12, Dart.SINGLE_12, Dart.SINGLE_9, Dart.SINGLE_9, Dart.TRIPLE_9, Dart.SINGLE_5, Dart.SINGLE_5, Dart.TRIPLE_5, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_12 to listOf(Dart.SINGLE_12, Dart.SINGLE_12, Dart.SINGLE_9, Dart.SINGLE_9, Dart.SINGLE_5, Dart.SINGLE_5, Dart.DOUBLE_12, Dart.TRIPLE_12),
            Dart.DOUBLE_12 to listOf(Dart.DOUBLE_12, Dart.ZERO, Dart.ZERO, Dart.SINGLE_12, Dart.SINGLE_9, Dart.SINGLE_5, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_11 to listOf(Dart.TRIPLE_11, Dart.SINGLE_11, Dart.SINGLE_11, Dart.SINGLE_14, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_8, Dart.SINGLE_8, Dart.TRIPLE_8, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_11 to listOf(Dart.SINGLE_11, Dart.SINGLE_11, Dart.SINGLE_14, Dart.SINGLE_14, Dart.SINGLE_8, Dart.SINGLE_8, Dart.DOUBLE_11, Dart.TRIPLE_11),
            Dart.DOUBLE_11 to listOf(Dart.DOUBLE_11, Dart.ZERO, Dart.ZERO, Dart.SINGLE_11, Dart.SINGLE_14, Dart.SINGLE_8, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_10 to listOf(Dart.TRIPLE_10, Dart.SINGLE_10, Dart.SINGLE_10, Dart.SINGLE_6, Dart.SINGLE_6, Dart.TRIPLE_6, Dart.SINGLE_15, Dart.SINGLE_15, Dart.TRIPLE_15, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_10 to listOf(Dart.SINGLE_10, Dart.SINGLE_10, Dart.SINGLE_6, Dart.SINGLE_6, Dart.SINGLE_15, Dart.SINGLE_15, Dart.DOUBLE_10, Dart.TRIPLE_10),
            Dart.DOUBLE_10 to listOf(Dart.DOUBLE_10, Dart.ZERO, Dart.ZERO, Dart.SINGLE_10, Dart.SINGLE_6, Dart.SINGLE_15, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_9 to listOf(Dart.TRIPLE_9, Dart.SINGLE_9, Dart.SINGLE_9, Dart.SINGLE_14, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_12, Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_9 to listOf(Dart.SINGLE_9, Dart.SINGLE_9, Dart.SINGLE_14, Dart.SINGLE_14, Dart.SINGLE_12, Dart.SINGLE_12, Dart.DOUBLE_9, Dart.TRIPLE_9),
            Dart.DOUBLE_9 to listOf(Dart.DOUBLE_9, Dart.ZERO, Dart.ZERO, Dart.SINGLE_9, Dart.SINGLE_14, Dart.SINGLE_12, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_8 to listOf(Dart.TRIPLE_8, Dart.SINGLE_8, Dart.SINGLE_8, Dart.SINGLE_11, Dart.SINGLE_11, Dart.TRIPLE_11, Dart.SINGLE_14, Dart.SINGLE_14, Dart.TRIPLE_14, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_8 to listOf(Dart.SINGLE_8, Dart.SINGLE_8, Dart.SINGLE_11, Dart.SINGLE_11, Dart.SINGLE_14, Dart.SINGLE_14, Dart.DOUBLE_8, Dart.TRIPLE_8),
            Dart.DOUBLE_8 to listOf(Dart.DOUBLE_8, Dart.ZERO, Dart.ZERO, Dart.SINGLE_8, Dart.SINGLE_11, Dart.SINGLE_14, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_7 to listOf(Dart.TRIPLE_7, Dart.SINGLE_7, Dart.SINGLE_7, Dart.SINGLE_16, Dart.SINGLE_16, Dart.TRIPLE_16, Dart.SINGLE_19, Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_7 to listOf(Dart.SINGLE_7, Dart.SINGLE_7, Dart.SINGLE_16, Dart.SINGLE_16, Dart.SINGLE_19, Dart.SINGLE_19, Dart.DOUBLE_7, Dart.TRIPLE_7),
            Dart.DOUBLE_7 to listOf(Dart.DOUBLE_7, Dart.ZERO, Dart.ZERO, Dart.SINGLE_7, Dart.SINGLE_16, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_6 to listOf(Dart.TRIPLE_6, Dart.SINGLE_6, Dart.SINGLE_6, Dart.SINGLE_10, Dart.SINGLE_10, Dart.TRIPLE_10, Dart.SINGLE_13, Dart.SINGLE_13, Dart.TRIPLE_13, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_6 to listOf(Dart.SINGLE_6, Dart.SINGLE_6, Dart.SINGLE_10, Dart.SINGLE_10, Dart.SINGLE_13, Dart.SINGLE_13, Dart.DOUBLE_6, Dart.TRIPLE_6),
            Dart.DOUBLE_6 to listOf(Dart.DOUBLE_6, Dart.ZERO, Dart.ZERO, Dart.SINGLE_6, Dart.SINGLE_10, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_5 to listOf(Dart.TRIPLE_5, Dart.SINGLE_5, Dart.SINGLE_5, Dart.SINGLE_12, Dart.SINGLE_12, Dart.TRIPLE_12, Dart.SINGLE_20, Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_5 to listOf(Dart.SINGLE_5, Dart.SINGLE_5, Dart.SINGLE_12, Dart.SINGLE_12, Dart.SINGLE_20, Dart.SINGLE_20, Dart.DOUBLE_5, Dart.TRIPLE_5),
            Dart.DOUBLE_5 to listOf(Dart.DOUBLE_5, Dart.ZERO, Dart.ZERO, Dart.SINGLE_5, Dart.SINGLE_12, Dart.SINGLE_20, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_4 to listOf(Dart.TRIPLE_4, Dart.SINGLE_4, Dart.SINGLE_4, Dart.SINGLE_10, Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_13, Dart.SINGLE_13, Dart.TRIPLE_13, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_4 to listOf(Dart.SINGLE_4, Dart.SINGLE_4, Dart.SINGLE_18, Dart.SINGLE_18, Dart.SINGLE_13, Dart.SINGLE_13, Dart.DOUBLE_4, Dart.TRIPLE_4),
            Dart.DOUBLE_4 to listOf(Dart.DOUBLE_4, Dart.ZERO, Dart.ZERO, Dart.SINGLE_4, Dart.TRIPLE_18, Dart.SINGLE_13, Dart.TRIPLE_13),

            Dart.TRIPLE_3 to listOf(Dart.TRIPLE_3, Dart.SINGLE_3, Dart.SINGLE_3, Dart.SINGLE_19, Dart.SINGLE_19, Dart.TRIPLE_19, Dart.SINGLE_17, Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_3 to listOf(Dart.SINGLE_3, Dart.SINGLE_3, Dart.SINGLE_19, Dart.SINGLE_19, Dart.SINGLE_17, Dart.SINGLE_17, Dart.DOUBLE_3, Dart.TRIPLE_3),
            Dart.DOUBLE_3 to listOf(Dart.DOUBLE_3, Dart.ZERO, Dart.ZERO, Dart.SINGLE_3, Dart.SINGLE_19, Dart.SINGLE_17, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_2 to listOf(Dart.TRIPLE_2, Dart.SINGLE_2, Dart.SINGLE_2, Dart.SINGLE_17, Dart.SINGLE_17, Dart.TRIPLE_17, Dart.SINGLE_15, Dart.SINGLE_15, Dart.TRIPLE_15, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_2 to listOf(Dart.SINGLE_2, Dart.SINGLE_2, Dart.SINGLE_17, Dart.SINGLE_17, Dart.SINGLE_15, Dart.SINGLE_15, Dart.DOUBLE_2, Dart.TRIPLE_2),
            Dart.DOUBLE_2 to listOf(Dart.DOUBLE_2, Dart.ZERO, Dart.ZERO, Dart.SINGLE_2, Dart.SINGLE_17, Dart.SINGLE_15, Dart.ZERO, Dart.ZERO),

            Dart.TRIPLE_1 to listOf(Dart.TRIPLE_1, Dart.SINGLE_1, Dart.SINGLE_1, Dart.SINGLE_20, Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_18, Dart.SINGLE_18, Dart.TRIPLE_18, Dart.SINGLE_1, Dart.SINGLE_2, Dart.SINGLE_3, Dart.SINGLE_4, Dart.SINGLE_5, Dart.SINGLE_6, Dart.SINGLE_7, Dart.SINGLE_8, Dart.SINGLE_9, Dart.SINGLE_10, Dart.SINGLE_11, Dart.SINGLE_12, Dart.SINGLE_13, Dart.SINGLE_14, Dart.SINGLE_15, Dart.SINGLE_16, Dart.SINGLE_17, Dart.SINGLE_18, Dart.SINGLE_19, Dart.SINGLE_20),
            Dart.SINGLE_1 to listOf(Dart.SINGLE_1, Dart.SINGLE_1, Dart.SINGLE_20, Dart.TRIPLE_20, Dart.SINGLE_18, Dart.SINGLE_18, Dart.DOUBLE_1, Dart.TRIPLE_1),
            Dart.DOUBLE_1 to listOf(Dart.DOUBLE_1, Dart.ZERO, Dart.ZERO, Dart.SINGLE_1, Dart.SINGLE_20, Dart.SINGLE_18, Dart.ZERO, Dart.ZERO)
    )

    fun go(next: Next, done: (Turn) -> Unit) {
        onBackground({
            if (next.player is Bot && next.state != State.MATCH) {

                logger.i("Target score: ${next.requiredScore}")
                val multiplier = (next.player as Bot).level

                val (_, turn1) = doTurn(next.requiredScore.score, Turn(), multiplier)
                val turn = if (turn1.total() >= next.requiredScore.score) {
                    turn1
                } else {
                    val (_, turn2) = doTurn(next.requiredScore.score, turn1, multiplier)
                    if (turn2.total() >= next.requiredScore.score) {
                        turn2
                    } else {
                        val (_, turn3) = doTurn(next.requiredScore.score, turn2, multiplier)
                        turn3
                    }
                }

                logger.i("Target thrown ${next.player}: $turn")
                onUi { done(turn) }
            }
        }, {})
    }

    private fun doTurn(required: Int, start: Turn, multiplier: Float): Pair<Dart, Turn> {
        val finish = finishUsecase.calculateInBack(required, start.dartsLeft(), start.total(), 0)
        logger.i("Target finish: $finish")
        val target = finish.split(" ").firstOrNull { it.isNotBlank() }
                ?: "T20" // TODO entreco - 2020-01-17: Estimate remainder
        logger.i("Target target: $target")
        val dart = generateSemiRandomDart(target, multiplier)
        logger.i("Target dart: $dart")
        val turn = start + dart
        return Pair(dart, turn)
    }

    private fun randomBucket(multiplier: Float): Int {
        val rand = Random().nextGaussian() / multiplier
        return when {
            rand < -1.5  -> 3
            rand < -1    -> 2
            rand < -0.25 -> 1
            rand < 0.25  -> 0
            rand < 1     -> 1
            rand < 1.5   -> 2
            else         -> 3
        }
    }

    fun generateSemiRandomDart(target: String, multiplier: Float): Dart {
        val targetDart = Dart.fromString(target)
        val randomBucket = randomBucket(multiplier)
        logger.i("Target dart: $randomBucket")
        val randomDart = when (randomBucket) {
            0    -> aiming[targetDart]?.take(ceil(targetDart.multiplier() / multiplier).toInt())?.random()
            1    -> aiming[targetDart]?.take(targetDart.multiplier() * 2)?.random()
            2    -> aiming[targetDart]?.random()
            else -> Dart.random()
        }

        return randomDart ?: Dart.random()
    }
}