package nl.entreco.domain.mastercaller

import androidx.annotation.VisibleForTesting

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCallerRequest(
    @VisibleForTesting val scored: Int = -1,
    private val start: Boolean = false,
    private val leg: Boolean = false,
    private val set: Boolean = false,
    private val match: Boolean = false
) {
    fun toSound(): Sound {

        return when {
            start -> FxStart
            leg -> FxLeg
            set -> FxSet
            match -> FxMatch
            else -> when (scored) {
                0 -> Fx00
                1 -> Fx01
                2 -> Fx02
                3 -> Fx03
                4 -> Fx04
                5 -> Fx05
                6 -> Fx06
                7 -> Fx07
                8 -> Fx08
                9 -> Fx09
                10 -> Fx10
                11 -> Fx11
                12 -> Fx12
                13 -> Fx13
                14 -> Fx14
                15 -> Fx15
                16 -> Fx16
                17 -> Fx17
                18 -> Fx18
                19 -> Fx19
                20 -> Fx20
                21 -> Fx21
                22 -> Fx22
                23 -> Fx23
                24 -> Fx24
                25 -> Fx25
                26 -> Fx26
                27 -> Fx27
                28 -> Fx28
                29 -> Fx29
                30 -> Fx30
                31 -> Fx31
                32 -> Fx32
                33 -> Fx33
                34 -> Fx34
                35 -> Fx35
                36 -> Fx36
                37 -> Fx37
                38 -> Fx38
                39 -> Fx39
                40 -> Fx40
                41 -> Fx41
                42 -> Fx42
                43 -> Fx43
                44 -> Fx44
                45 -> Fx45
                46 -> Fx46
                47 -> Fx47
                48 -> Fx48
                49 -> Fx49
                50 -> Fx50
                51 -> Fx51
                52 -> Fx52
                53 -> Fx53
                54 -> Fx54
                55 -> Fx55
                56 -> Fx56
                57 -> Fx57
                58 -> Fx58
                59 -> Fx59
                60 -> Fx60
                61 -> Fx61
                62 -> Fx62
                63 -> Fx63
                64 -> Fx64
                65 -> Fx65
                66 -> Fx66
                67 -> Fx67
                68 -> Fx68
                69 -> Fx69
                70 -> Fx70
                71 -> Fx71
                72 -> Fx72
                73 -> Fx73
                74 -> Fx74
                75 -> Fx75
                76 -> Fx76
                77 -> Fx77
                78 -> Fx78
                79 -> Fx79
                80 -> Fx80
                81 -> Fx81
                82 -> Fx82
                83 -> Fx83
                84 -> Fx84
                85 -> Fx85
                86 -> Fx86
                87 -> Fx87
                88 -> Fx88
                89 -> Fx89
                90 -> Fx90
                91 -> Fx91
                92 -> Fx92
                93 -> Fx93
                94 -> Fx94
                95 -> Fx95
                96 -> Fx96
                97 -> Fx97
                98 -> Fx98
                99 -> Fx99
                100 -> Fx100
                101 -> Fx101
                102 -> Fx102
                103 -> Fx103
                104 -> Fx104
                105 -> Fx105
                106 -> Fx106
                107 -> Fx107
                108 -> Fx108
                109 -> Fx109
                110 -> Fx110
                111 -> Fx111
                112 -> Fx112
                113 -> Fx113
                114 -> Fx114
                115 -> Fx115
                116 -> Fx116
                117 -> Fx117
                118 -> Fx118
                119 -> Fx119
                120 -> Fx120
                121 -> Fx121
                122 -> Fx122
                123 -> Fx123
                124 -> Fx124
                125 -> Fx125
                126 -> Fx126
                127 -> Fx127
                128 -> Fx128
                129 -> Fx129
                130 -> Fx130
                131 -> Fx131
                132 -> Fx132
                133 -> Fx133
                134 -> Fx134
                135 -> Fx135
                136 -> Fx136
                137 -> Fx137
                138 -> Fx138
                139 -> Fx139
                140 -> Fx140
                141 -> Fx141
                142 -> Fx142
                143 -> Fx143
                144 -> Fx144
                145 -> Fx145
                146 -> Fx146
                147 -> Fx147
                148 -> Fx148
                149 -> Fx149
                150 -> Fx150
                151 -> Fx151
                152 -> Fx152
                153 -> Fx153
                154 -> Fx154
                155 -> Fx155
                156 -> Fx156
                157 -> Fx157
                158 -> Fx158
                159 -> Fx159
                160 -> Fx160
                161 -> Fx161
                162 -> Fx162
                163 -> Fx163
                164 -> Fx164
                165 -> Fx165
                166 -> Fx166
                167 -> Fx167
                168 -> Fx168
                169 -> Fx169
                170 -> Fx170
                171 -> Fx171
                174 -> Fx174
                177 -> Fx177
                180 -> Fx180
                else -> None
            }
        }
    }
}