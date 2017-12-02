package nl.entreco.dartsscorecard.play.input

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.widget.TextView
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.PlayerListener
import nl.entreco.domain.Analytics
import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Next
import nl.entreco.domain.play.model.Turn
import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import javax.inject.Inject

/**
 * Created by Entreco on 19/11/2017.
 */
open class InputViewModel @Inject constructor(private val analytics: Analytics) : BaseViewModel(), PlayerListener {

    val toggle = ObservableBoolean(false)
    val current = ObservableField<Player>(NoPlayer())
    val scoredTxt = ObservableField<String>("")
    var count = 0
    private var turn = Turn()
    private var nextUp: Next? = null

    fun entered(score: Int) {
        scoredTxt.set(scoredTxt.get().plus(score.toString()))
    }

    fun back() {
        scoredTxt.set(scoredTxt.get().dropLast(1))
    }

    fun submitRandom(listener: InputListener) {

        if (nextUp == null || nextUp?.state == State.MATCH) return

        if (toggle.get()) {
            submitSingles(listener)
        } else {
            submitAll(listener)
        }
    }

    fun tryToSubmit(input: TextView, listener: InputListener) {
        val scored = try {
            input.text.toString().toInt()
        } catch (err: Exception) {
            0
        }
        val turn = estimate(scored)
        submit(turn, listener)
    }

    private fun estimate(scored: Int): Turn {
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
            158 -> Turn() // "T20 T20 D19")
            157 -> Turn() // "T20 T19 D20")
            156 -> Turn() // "T20 T20 D18")
            155 -> Turn() // "T20 T19 D19")
            154 -> Turn() // "T20 T18 D20")
            153 -> Turn() // "T20 T19 D18")
            152 -> Turn() // "T20 T20 D16")
            151 -> Turn() // "T20 T17 D20")
            150 -> Turn() // "T20 T18 D18")
            149 -> Turn() // "T20 T19 D16")
            148 -> Turn() // "T20 T16 D20")
            147 -> Turn() // "T19 T18 D18")
            146 -> Turn() // "T20 T18 D16")
            145 -> Turn() // "T20 T15 D20")
            144 -> Turn() // "T20 T20 D12")
            143 -> Turn() // "T20 T17 D16")
            142 -> Turn() // "T20 T14 D20")
            141 -> Turn() // "T20 T19 D12")
            140 -> Turn(Dart.TRIPLE_20, Dart.TRIPLE_16, Dart.DOUBLE_16) // "T20 T16 D16")
            139 -> Turn() // "T19 T14 D20")
            138 -> Turn() // "T20 T18 D12")
            137 -> Turn() // "T19 T16 D16")
            136 -> Turn() // "T20 T20 D8")
            135 -> Turn() // "T20 T17 D12")
            134 -> Turn() // "T20 T14 D16")
            133 -> Turn() // "T20 T19 D8")
            132 -> Turn() // "T20 T16 D12")
            131 -> Turn() // "T20 T13 D16")
            130 -> Turn() // "T20 20 BULL")
            129 -> Turn() // "T19 T16 D12")
            128 -> Turn() // "T18 T14 D16")
            127 -> Turn() // "T20 T17 D8")
            126 -> Turn() // "T19 T19 D6")
            125 -> Turn() // "S.BULL T20 D20")
            124 -> Turn() // "T20 T16 D8")
            123 -> Turn() // "T19 T16 D9")
            122 -> Turn() // "T18 T20 D4")
            121 -> Turn(Dart.TRIPLE_17, Dart.TRIPLE_10, Dart.DOUBLE_20) // "T17 T10 D20")
            120 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_20, Dart.DOUBLE_20) // "T20 20 D20")
            119 -> Turn() // "T19 T10 D16")
            118 -> Turn() // "T20 18 D20")
            117 -> Turn() // "T20 17 D20")
            116 -> Turn() // "T20 16 D20")
            115 -> Turn() // "T20 15 D20")
            114 -> Turn() // "T20 14 D20")
            113 -> Turn() // "T20 13 D20")
            112 -> Turn() // "T20 12 D20")
            111 -> Turn() // "T20 19 D16")
            110 -> Turn() // "T20 18 D16")
            109 -> Turn() // "T19 20 D16")
            108 -> Turn() // "T20 16 D16")
            107 -> Turn() // "T19 18 D16")
            106 -> Turn() // "T20 14 D16")
            105 -> Turn() // "T19 16 D16")
            104 -> Turn() // "T18 18 D16")
            103 -> Turn() // "T20 3 D20")
            102 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_10, Dart.DOUBLE_16) // "T20 10 D16")
            101 -> Turn(Dart.TRIPLE_20, Dart.SINGLE_1, Dart.DOUBLE_20) // "T20 1 D20")
            100 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_20) // "T20 D20")
            99 -> Turn(Dart.TRIPLE_19, Dart.SINGLE_10, Dart.DOUBLE_16) //"T19 10 D16"
            98 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_19) //"T19 D19"
            97 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_20) //"T19 D20"
            96 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_18) //"T20 D18"
            95 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_19) //"T19 D19"
            94 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_20) //"T18 D20"
            93 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_18) //"T19 D18"
            92 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_16) //"T20 D16"
            91 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_20) //"T17 D20"
            90 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_15) //"T20 D15"
            89 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_16) //"T19 D16"
            88 -> Turn(Dart.TRIPLE_16, Dart.DOUBLE_20) //"T16 D20"
            87 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_18) //"T17 D18"
            86 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_16) //"T18 D16"
            85 -> Turn(Dart.TRIPLE_15, Dart.DOUBLE_20) //"T15 D20"
            84 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_12) //"T20 D12"
            83 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_16) //"T17 D16"
            82 -> Turn(Dart.TRIPLE_14, Dart.DOUBLE_20) //"T14 D20"
            81 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_12) //"T19 D12"
            80 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_10) //"T20 D10"
            79 -> Turn(Dart.TRIPLE_13, Dart.DOUBLE_20) //"T13 D20"
            78 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_12) //"T18 D12"
            77 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_10) //"T19 D10"
            76 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_8) //"T20 D8"
            75 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_12) //"T17 D12"
            74 -> Turn(Dart.TRIPLE_18, Dart.DOUBLE_10) //"T18 D10"
            73 -> Turn(Dart.TRIPLE_19, Dart.DOUBLE_8) //"T19 D8"
            72 -> Turn(Dart.TRIPLE_12, Dart.DOUBLE_18) //"T12 D18"
            71 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_10) //"T17 D10"
            70 -> Turn(Dart.TRIPLE_10, Dart.DOUBLE_20) //"T10 D20"
            69 -> Turn(Dart.TRIPLE_13, Dart.DOUBLE_15) //"T13 D15"
            68 -> Turn(Dart.TRIPLE_20, Dart.DOUBLE_4) //"T20 D4"
            67 -> Turn(Dart.TRIPLE_17, Dart.DOUBLE_8) //"T17 D8"
            66 -> Turn(Dart.TRIPLE_10, Dart.DOUBLE_18) //"T10 D18"
            65 -> Turn(Dart.SINGLE_BULL, Dart.DOUBLE_20) //"S.BULL D20"
            64 -> Turn(Dart.TRIPLE_16, Dart.DOUBLE_8) //"T16 D8"
            63 -> Turn(Dart.TRIPLE_13, Dart.DOUBLE_12) //"T13 D12"
            62 -> Turn(Dart.TRIPLE_10, Dart.DOUBLE_16) //"T10 D16"
            61 -> Turn(Dart.SINGLE_BULL, Dart.DOUBLE_18) //"25 D18"
            60 -> Turn(Dart.SINGLE_20, Dart.DOUBLE_20) //"20 D20"
            59 -> Turn(Dart.SINGLE_19, Dart.DOUBLE_20) //"19 D20"
            58 -> Turn(Dart.SINGLE_18, Dart.DOUBLE_20) //"18 D20"
            57 -> Turn(Dart.SINGLE_17, Dart.DOUBLE_20) //"17 D20"
            56 -> Turn(Dart.SINGLE_16, Dart.DOUBLE_4) //"T16 D4"
            55 -> Turn(Dart.SINGLE_15, Dart.DOUBLE_20) //"15 D20"
            54 -> Turn(Dart.SINGLE_14, Dart.DOUBLE_20) //"14 D20"
            53 -> Turn(Dart.SINGLE_13, Dart.DOUBLE_20) //"13 D20"
            52 -> Turn(Dart.SINGLE_12, Dart.DOUBLE_8) //"T12 D8"
            51 -> Turn(Dart.SINGLE_11, Dart.DOUBLE_20) //"11 D20"
            50 -> Turn(Dart.BULL) //"BULL" // Special case
            49 -> Turn(Dart.SINGLE_9, Dart.DOUBLE_20) //"9 D20"
            48 -> Turn(Dart.SINGLE_16, Dart.DOUBLE_16) //"16 D16"
            47 -> Turn(Dart.SINGLE_15, Dart.DOUBLE_16) //"15 D16"
            46 -> Turn(Dart.SINGLE_6, Dart.DOUBLE_20) //"6 D20"
            45 -> Turn(Dart.SINGLE_13, Dart.DOUBLE_16) //"13 D16"
            44 -> Turn(Dart.SINGLE_12, Dart.DOUBLE_16) //"12 D16"
            43 -> Turn(Dart.SINGLE_11, Dart.DOUBLE_16) //"11 D16"
            42 -> Turn(Dart.SINGLE_10, Dart.DOUBLE_16) //"10 D16"
            41 -> Turn(Dart.SINGLE_9, Dart.DOUBLE_16) //"9 D16"
            40 -> Turn(Dart.DOUBLE_20) //"D20"
            39 -> Turn(Dart.SINGLE_7, Dart.DOUBLE_16) //"7 D16"
            38 -> Turn(Dart.DOUBLE_19) //"D19"
            37 -> Turn(Dart.SINGLE_5) //"5 D16"
            36 -> Turn(Dart.DOUBLE_18) //"D18"
            35 -> Turn(Dart.SINGLE_3, Dart.DOUBLE_16) //"3 D16"
            34 -> Turn(Dart.DOUBLE_17) //"D17"
            33 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_16) //"1 D16"
            32 -> Turn(Dart.DOUBLE_16) //"D16"
            31 -> Turn(Dart.SINGLE_15, Dart.DOUBLE_8) //"15 D8"
            30 -> Turn(Dart.DOUBLE_15) //"D15"
            29 -> Turn(Dart.SINGLE_13, Dart.DOUBLE_8) //"13 D8"
            28 -> Turn(Dart.DOUBLE_14) //"D14"
            27 -> Turn(Dart.SINGLE_19, Dart.DOUBLE_4) //"19 D4"
            26 -> Turn(Dart.DOUBLE_13) //"D13"
            25 -> Turn(Dart.SINGLE_17, Dart.DOUBLE_4) //"17 D4"
            24 -> Turn(Dart.DOUBLE_12) //"D12"
            23 -> Turn(Dart.SINGLE_7, Dart.DOUBLE_8) //"7 D8"
            22 -> Turn(Dart.DOUBLE_11) //"D11"
            21 -> Turn(Dart.SINGLE_5, Dart.DOUBLE_8) //"5 D8"
            20 -> Turn(Dart.DOUBLE_10) //"D10"
            19 -> Turn(Dart.SINGLE_11, Dart.DOUBLE_4) //"11 D4"
            18 -> Turn(Dart.DOUBLE_9) //"D9"
            17 -> Turn(Dart.SINGLE_9, Dart.DOUBLE_4) //"9 D4"
            16 -> Turn(Dart.DOUBLE_8) //"D8"
            15 -> Turn(Dart.SINGLE_7, Dart.DOUBLE_4) //"7 D4"
            14 -> Turn(Dart.DOUBLE_7) //"D7"
            13 -> Turn(Dart.SINGLE_5, Dart.DOUBLE_4) //"5 D4"
            12 -> Turn(Dart.DOUBLE_6) //"D6"
            11 -> Turn(Dart.SINGLE_3, Dart.DOUBLE_4) //"3 D4"
            10 -> Turn(Dart.DOUBLE_5) //"D5"
            9 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_4)
            8 -> Turn(Dart.DOUBLE_4)
            7 -> Turn(Dart.SINGLE_3, Dart.DOUBLE_2)
            6 -> Turn(Dart.DOUBLE_3)
            5 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_2)
            4 -> Turn(Dart.DOUBLE_2)
            3 -> Turn(Dart.SINGLE_1, Dart.DOUBLE_1)
            2 -> Turn(Dart.DOUBLE_1)
            1 -> Turn(Dart.SINGLE_1)
            0 -> Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO)
            else -> Turn()
        }
    }

    private fun submitAll(listener: InputListener) {
        val turn = Turn(Dart.random(), Dart.random(), Dart.random())
        submit(turn, listener)
    }

    private fun submitSingles(listener: InputListener) {
        when {
            firstDart() -> {
                turn += Dart.random()
                scoredTxt.set(turn.total().toString())
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
            secondDart() -> {
                turn += Dart.random()
                scoredTxt.set(turn.total().toString())
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
            }
            else -> {
                turn += Dart.random()
                listener.onDartThrown(turn.copy(), nextUp?.player!!)
                submit(turn.copy(), listener)

                turn = Turn()
            }
        }
        count++
    }

    private fun submit(turn: Turn, listener: InputListener) {
        scoredTxt.set(turn.total().toString())
        listener.onTurnSubmitted(turn.copy(), nextUp?.player!!)
        analytics.trackAchievement("scored: $turn")
    }

    private fun secondDart() = count % 3 == 1

    private fun firstDart() = count % 3 == 0

    override fun onNext(next: Next) {
        scoredTxt.set("")
        nextUp = next
        current.set(next.player)
    }
}