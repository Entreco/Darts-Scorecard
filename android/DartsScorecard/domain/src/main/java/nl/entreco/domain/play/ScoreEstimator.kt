package nl.entreco.domain.play

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Turn
import javax.inject.Inject

/**
 * Created by Entreco on 02/12/2017.
 */
class ScoreEstimator @Inject constructor() {

    fun guess(scored: Int, singles: Boolean): Turn {
        return if (singles) guessSingle(scored) else guessThree(scored)
    }

    private fun guessSingle(scored: Int): Turn {
        return when (scored) {
            60 -> Turn(Dart.TRIPLE_20)
            57 -> Turn(Dart.TRIPLE_19)
            54 -> Turn(Dart.TRIPLE_18)
            51 -> Turn(Dart.TRIPLE_17)
            50 -> Turn(Dart.BULL)
            48 -> Turn(Dart.TRIPLE_16)
            45 -> Turn(Dart.TRIPLE_15)
            42 -> Turn(Dart.TRIPLE_14)
            40 -> Turn(Dart.DOUBLE_20)
            38 -> Turn(Dart.DOUBLE_19)
            36 -> Turn(Dart.DOUBLE_18)
            34 -> Turn(Dart.DOUBLE_17)
            33 -> Turn(Dart.TRIPLE_11)
            32 -> Turn(Dart.DOUBLE_16)
            30 -> Turn(Dart.DOUBLE_15)
            28 -> Turn(Dart.DOUBLE_14)
            27 -> Turn(Dart.TRIPLE_9)
            26 -> Turn(Dart.DOUBLE_13)
            25 -> Turn(Dart.SINGLE_BULL)
            24 -> Turn(Dart.DOUBLE_12)
            22 -> Turn(Dart.DOUBLE_11)
            21 -> Turn(Dart.TRIPLE_7)
            20 -> Turn(Dart.DOUBLE_10)
            19 -> Turn(Dart.SINGLE_19)
            18 -> Turn(Dart.DOUBLE_9)
            17 -> Turn(Dart.SINGLE_17)
            16 -> Turn(Dart.DOUBLE_8)
            15 -> Turn(Dart.SINGLE_15)
            14 -> Turn(Dart.DOUBLE_7)
            13 -> Turn(Dart.SINGLE_13)
            12 -> Turn(Dart.DOUBLE_6)
            11 -> Turn(Dart.SINGLE_11)
            10 -> Turn(Dart.DOUBLE_5)
            9 -> Turn(Dart.SINGLE_9)
            8 -> Turn(Dart.DOUBLE_4)
            7 -> Turn(Dart.SINGLE_7)
            6 -> Turn(Dart.DOUBLE_3)
            5 -> Turn(Dart.SINGLE_5)
            4 -> Turn(Dart.DOUBLE_2)
            3 -> Turn(Dart.SINGLE_3)
            2 -> Turn(Dart.DOUBLE_1)
            1 -> Turn(Dart.SINGLE_1)
            else -> Turn(Dart.ZERO)
        }
    }

    private fun guessThree(scored: Int): Turn {
        return when (scored) {
            180 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20)
            177 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_19)
            174 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_18)
            171 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_19, Dart.TRIPLE_19)
            170 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.BULL) // "T20 T20 BULL")
            167 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.BULL) // "T20 T19 BULL")
            164 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_18, Dart.BULL) // "T20 T18 BULL")
            161 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_17, Dart.BULL) // "T20 T17 BULL")
            160 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.DOUBLE_20) // "T20 T20 D20")
            158 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.DOUBLE_19) // "T20 T20 D19")
            157 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_20) // "T20 T19 D20")
            156 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.DOUBLE_18) // "T20 T20 D18")
            155 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_19) // "T20 T19 D19")
            154 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_18, Dart.DOUBLE_20) // "T20 T18 D20")
            153 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_18) // "T20 T19 D18")
            152 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.DOUBLE_16) // "T20 T20 D16")
            151 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_17, Dart.DOUBLE_20) // "T20 T17 D20")
            150 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_18, Dart.DOUBLE_18) // "T20 T18 D18")
            149 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_16) // "T20 T19 D16")
            148 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_16, Dart.DOUBLE_20) // "T20 T16 D20")
            147 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_18, Dart.DOUBLE_18) // "T19 T18 D18")
            146 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_18, Dart.DOUBLE_16) // "T20 T18 D16")
            145 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_15, Dart.DOUBLE_20) // "T20 T15 D20")
            144 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.DOUBLE_12) // "T20 T20 D12")
            143 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_17, Dart.DOUBLE_16) // "T20 T17 D16")
            142 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_14, Dart.DOUBLE_20) // "T20 T14 D20")
            141 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_12) // "T20 T19 D12")
            140 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_16, Dart.DOUBLE_16) // "T20 T16 D16")
            139 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_14, Dart.DOUBLE_20) // "T19 T14 D20")
            138 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_18, Dart.DOUBLE_12) // "T20 T18 D12")
            137 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_16, Dart.DOUBLE_16) // "T19 T16 D16")
            136 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.DOUBLE_8) // "T20 T20 D8")
            135 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_17, Dart.DOUBLE_12) // "T20 T17 D12")
            134 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_14, Dart.DOUBLE_16) // "T20 T14 D16")
            133 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.DOUBLE_8) // "T20 T19 D8")
            132 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_16, Dart.DOUBLE_12) // "T20 T16 D12")
            131 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_13, Dart.DOUBLE_16) // "T20 T13 D16")
            130 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_20, Dart.BULL) // "T20 20 BULL")
            129 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_16, Dart.DOUBLE_12) // "T19 T16 D12")
            128 -> Turn(Dart.TRIPLE_18, Dart.TRIPLE_14, Dart.DOUBLE_16) // "T18 T14 D16")
            127 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_17, Dart.DOUBLE_8) // "T20 T17 D8")
            126 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_19, Dart.DOUBLE_6) // "T19 T19 D6")
            125 -> Turn(Dart.SINGLE_BULL, Dart.TRIPLE_20, Dart.DOUBLE_20) // "S.BULL T20 D20")
            124 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_16, Dart.DOUBLE_8) // "T20 T16 D8")
            123 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_16, Dart.DOUBLE_9) // "T19 T16 D9")
            122 -> Turn(Dart.TRIPLE_18, Dart.TRIPLE_20, Dart.DOUBLE_4) // "T18 T20 D4")
            121 -> Turn(Dart.TRIPLE_17, Dart.TRIPLE_10, Dart.DOUBLE_20) // "T17 T10 D20")
            120 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_20, Dart.DOUBLE_20) // "T20 20 D20")
            119 -> Turn(Dart.TRIPLE_19, Dart.TRIPLE_10, Dart.DOUBLE_16) // "T19 T10 D16")
            118 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_18, Dart.DOUBLE_20) // "T20 18 D20")
            117 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_17, Dart.DOUBLE_20) // "T20 17 D20")
            116 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_16, Dart.DOUBLE_20) // "T20 16 D20")
            115 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_15, Dart.DOUBLE_20) // "T20 15 D20")
            114 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_14, Dart.DOUBLE_20) // "T20 14 D20")
            113 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_13, Dart.DOUBLE_20) // "T20 13 D20")
            112 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_12, Dart.DOUBLE_20) // "T20 12 D20")
            111 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_19, Dart.DOUBLE_16) // "T20 19 D16")
            110 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_18, Dart.DOUBLE_16) // "T20 18 D16")
            109 -> Turn(Dart.TRIPLE_19, Dart.SINGLE_20, Dart.DOUBLE_16) // "T19 20 D16")
            108 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_16, Dart.DOUBLE_16) // "T20 16 D16")
            107 -> Turn(Dart.TRIPLE_19, Dart.SINGLE_18, Dart.DOUBLE_16) // "T19 18 D16")
            106 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_14, Dart.DOUBLE_16) // "T20 14 D16")
            105 -> Turn(Dart.TRIPLE_19, Dart.SINGLE_16, Dart.DOUBLE_16) // "T19 16 D16")
            104 -> Turn(Dart.TRIPLE_18, Dart.SINGLE_18, Dart.DOUBLE_16) // "T18 18 D16")
            103 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_3, Dart.DOUBLE_20) // "T20 3 D20")
            102 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_10, Dart.DOUBLE_16) // "T20 10 D16")
            101 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_1, Dart.DOUBLE_20) // "T20 1 D20")
            100 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_20, Dart.ZERO) // "T20 D20")
            99 -> Turn(Dart.TRIPLE_19, Dart.SINGLE_10, Dart.DOUBLE_16) //"T19 10 D16"
            98 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_19, Dart.ZERO) //"T20 D19"
            97 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_20, Dart.ZERO) //"T19 D20"
            96 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_18, Dart.ZERO) //"T20 D18"
            95 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_19, Dart.ZERO) //"T19 D19"
            94 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_20, Dart.ZERO) //"T18 D20"
            93 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_18, Dart.ZERO) //"T19 D18"
            92 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_16, Dart.ZERO) //"T20 D16"
            91 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_20, Dart.ZERO) //"T17 D20"
            90 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_15, Dart.ZERO) //"T20 D15"
            89 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_16, Dart.ZERO) //"T19 D16"
            88 -> Turn(Dart.TRIPLE_16, Dart.DOUBLE_20, Dart.ZERO) //"T16 D20"
            87 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_18, Dart.ZERO) //"T17 D18"
            86 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_16, Dart.ZERO) //"T18 D16"
            85 -> Turn(Dart.TRIPLE_15, Dart.DOUBLE_20, Dart.ZERO) //"T15 D20"
            84 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_12, Dart.ZERO) //"T20 D12"
            83 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_16, Dart.ZERO) //"T17 D16"
            82 -> Turn(Dart.TRIPLE_14, Dart.DOUBLE_20, Dart.ZERO) //"T14 D20"
            81 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_12, Dart.ZERO) //"T19 D12"
            80 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_10, Dart.ZERO) //"T20 D10"
            79 -> Turn(Dart.TRIPLE_13, Dart.DOUBLE_20, Dart.ZERO) //"T13 D20"
            78 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_12, Dart.ZERO) //"T18 D12"
            77 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_10, Dart.ZERO) //"T19 D10"
            76 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_8, Dart.ZERO) //"T20 D8"
            75 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_12, Dart.ZERO) //"T17 D12"
            74 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_10, Dart.ZERO) //"T18 D10"
            73 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_8, Dart.ZERO) //"T19 D8"
            72 -> Turn(Dart.TRIPLE_12, Dart.DOUBLE_18, Dart.ZERO) //"T12 D18"
            71 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_10, Dart.ZERO) //"T17 D10"
            70 -> Turn(Dart.TRIPLE_10, Dart.DOUBLE_20, Dart.ZERO) //"T10 D20"
            69 -> Turn(Dart.TRIPLE_13, Dart.DOUBLE_15, Dart.ZERO) //"T13 D15"
            68 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_4, Dart.ZERO) //"T20 D4"
            67 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_8, Dart.ZERO) //"T17 D8"
            66 -> Turn(Dart.TRIPLE_10, Dart.DOUBLE_18, Dart.ZERO) //"T10 D18"
            65 -> Turn(Dart.SINGLE_BULL, Dart.DOUBLE_20, Dart.ZERO) //"S.BULL D20"
            64 -> Turn(Dart.TRIPLE_16, Dart.DOUBLE_8, Dart.ZERO) //"T16 D8"
            63 -> Turn(Dart.TRIPLE_13, Dart.DOUBLE_12, Dart.ZERO) //"T13 D12"
            62 -> Turn(Dart.TRIPLE_10, Dart.DOUBLE_16, Dart.ZERO) //"T10 D16"
            61 -> Turn(Dart.SINGLE_BULL, Dart.DOUBLE_18, Dart.ZERO) //"25 D18"
            60 -> Turn(Dart.SINGLE_20, Dart.DOUBLE_20, Dart.ZERO) //"20 D20"
            59 -> Turn(Dart.SINGLE_19, Dart.DOUBLE_20, Dart.ZERO) //"19 D20"
            58 -> Turn(Dart.SINGLE_18, Dart.DOUBLE_20, Dart.ZERO) //"18 D20"
            57 -> Turn(Dart.SINGLE_17, Dart.DOUBLE_20, Dart.ZERO) //"17 D20"
            56 -> Turn(Dart.TRIPLE_16, Dart.DOUBLE_4, Dart.ZERO) //"T16 D4"
            55 -> Turn(Dart.SINGLE_15, Dart.DOUBLE_20, Dart.ZERO) //"15 D20"
            54 -> Turn(Dart.SINGLE_14, Dart.DOUBLE_20, Dart.ZERO) //"14 D20"
            53 -> Turn(Dart.SINGLE_13, Dart.DOUBLE_20, Dart.ZERO) //"13 D20"
            52 -> Turn(Dart.TRIPLE_12, Dart.DOUBLE_8, Dart.ZERO) //"T12 D8"
            51 -> Turn(Dart.SINGLE_11, Dart.DOUBLE_20, Dart.ZERO) //"11 D20"
            50 -> Turn(Dart.BULL, Dart.ZERO, Dart.ZERO) //"BULL" // Special case
            49 -> Turn(Dart.SINGLE_9, Dart.DOUBLE_20, Dart.ZERO) //"9 D20"
            48 -> Turn(Dart.SINGLE_16, Dart.DOUBLE_16, Dart.ZERO) //"16 D16"
            47 -> Turn(Dart.SINGLE_15, Dart.DOUBLE_16, Dart.ZERO) //"15 D16"
            46 -> Turn(Dart.SINGLE_6, Dart.DOUBLE_20, Dart.ZERO) //"6 D20"
            45 -> Turn(Dart.SINGLE_13, Dart.DOUBLE_16, Dart.ZERO) //"13 D16"
            44 -> Turn(Dart.SINGLE_12, Dart.DOUBLE_16, Dart.ZERO) //"12 D16"
            43 -> Turn(Dart.SINGLE_11, Dart.DOUBLE_16, Dart.ZERO) //"11 D16"
            42 -> Turn(Dart.SINGLE_10, Dart.DOUBLE_16, Dart.ZERO) //"10 D16"
            41 -> Turn(Dart.SINGLE_9, Dart.DOUBLE_16, Dart.ZERO) //"9 D16"
            40 -> Turn(Dart.DOUBLE_20, Dart.ZERO, Dart.ZERO) //"D20"
            39 -> Turn(Dart.SINGLE_7, Dart.DOUBLE_16, Dart.ZERO) //"7 D16"
            38 -> Turn(Dart.DOUBLE_19, Dart.ZERO, Dart.ZERO) //"D19"
            37 -> Turn(Dart.SINGLE_5, Dart.DOUBLE_16, Dart.ZERO) //"5 D16"
            36 -> Turn(Dart.DOUBLE_18, Dart.ZERO, Dart.ZERO) //"D18"
            35 -> Turn(Dart.SINGLE_3, Dart.DOUBLE_16, Dart.ZERO) //"3 D16"
            34 -> Turn(Dart.DOUBLE_17, Dart.ZERO, Dart.ZERO) //"D17"
            33 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_16, Dart.ZERO) //"1 D16"
            32 -> Turn(Dart.DOUBLE_16, Dart.ZERO, Dart.ZERO) //"D16"
            31 -> Turn(Dart.SINGLE_15, Dart.DOUBLE_8, Dart.ZERO) //"15 D8"
            30 -> Turn(Dart.DOUBLE_15, Dart.ZERO, Dart.ZERO) //"D15"
            29 -> Turn(Dart.SINGLE_13, Dart.DOUBLE_8, Dart.ZERO) //"13 D8"
            28 -> Turn(Dart.DOUBLE_14, Dart.ZERO, Dart.ZERO) //"D14"
            27 -> Turn(Dart.SINGLE_19, Dart.DOUBLE_4, Dart.ZERO) //"19 D4"
            26 -> Turn(Dart.DOUBLE_13, Dart.ZERO, Dart.ZERO) //"D13"
            25 -> Turn(Dart.SINGLE_17, Dart.DOUBLE_4, Dart.ZERO) //"17 D4"
            24 -> Turn(Dart.DOUBLE_12, Dart.ZERO, Dart.ZERO) //"D12"
            23 -> Turn(Dart.SINGLE_7, Dart.DOUBLE_8, Dart.ZERO) //"7 D8"
            22 -> Turn(Dart.DOUBLE_11, Dart.ZERO, Dart.ZERO) //"D11"
            21 -> Turn(Dart.SINGLE_5, Dart.DOUBLE_8, Dart.ZERO) //"5 D8"
            20 -> Turn(Dart.DOUBLE_10, Dart.ZERO, Dart.ZERO) //"D10"
            19 -> Turn(Dart.SINGLE_11, Dart.DOUBLE_4, Dart.ZERO) //"11 D4"
            18 -> Turn(Dart.DOUBLE_9, Dart.ZERO, Dart.ZERO) //"D9"
            17 -> Turn(Dart.SINGLE_9, Dart.DOUBLE_4, Dart.ZERO) //"9 D4"
            16 -> Turn(Dart.DOUBLE_8, Dart.ZERO, Dart.ZERO) //"D8"
            15 -> Turn(Dart.SINGLE_7, Dart.DOUBLE_4, Dart.ZERO) //"7 D4"
            14 -> Turn(Dart.DOUBLE_7, Dart.ZERO, Dart.ZERO) //"D7"
            13 -> Turn(Dart.SINGLE_5, Dart.DOUBLE_4, Dart.ZERO) //"5 D4"
            12 -> Turn(Dart.DOUBLE_6, Dart.ZERO, Dart.ZERO) //"D6"
            11 -> Turn(Dart.SINGLE_3, Dart.DOUBLE_4, Dart.ZERO) //"3 D4"
            10 -> Turn(Dart.DOUBLE_5, Dart.ZERO, Dart.ZERO) //"D5"
            9 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_4, Dart.ZERO)
            8 -> Turn(Dart.DOUBLE_4, Dart.ZERO, Dart.ZERO)
            7 -> Turn(Dart.SINGLE_3, Dart.DOUBLE_2, Dart.ZERO)
            6 -> Turn(Dart.DOUBLE_3, Dart.ZERO, Dart.ZERO)
            5 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_2, Dart.ZERO)
            4 -> Turn(Dart.DOUBLE_2, Dart.ZERO, Dart.ZERO)
            3 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_1, Dart.ZERO)
            2 -> Turn(Dart.DOUBLE_1, Dart.ZERO, Dart.ZERO)
            1 -> Turn(Dart.SINGLE_1, Dart.ZERO, Dart.ZERO)
            0 -> Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO)
            else -> Turn()
        }
    }

    fun atDouble(turn: Turn, required: Int): Int {
        if (required > 170) return 0
        var outs = find(required)
        outs += find(required - turn.d1.points())
        outs += find(required - turn.d1.points() - turn.d2.points())
        return outs
    }

    private val range = 40 downTo 2 step 2
    private fun find(required: Int): Int {
        return if (range.contains(required) || required == 50) 1
        else 0
    }
}
