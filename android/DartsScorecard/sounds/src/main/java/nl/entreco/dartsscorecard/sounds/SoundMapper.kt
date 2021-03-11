package nl.entreco.dartsscorecard.sounds

import androidx.annotation.RawRes
import nl.entreco.domain.mastercaller.*

/**
 * Created by entreco on 14/03/2018.
 */
class SoundMapper {

    @RawRes
    fun toRaw(sound: Sound): Int {
        return when (sound) {
            is None -> 0
            is FxStart -> R.raw.dsc_proletsplaydarts
            is FxLeg -> R.raw.dsc_proleg
            is FxSet -> R.raw.dsc_proset
            is FxMatch -> R.raw.dsc_progameshot
            is Fx00 -> R.raw.dsc_pro0
            is Fx01 -> R.raw.dsc_pro1
            is Fx02 -> R.raw.dsc_pro2
            is Fx03 -> R.raw.dsc_pro3
            is Fx04 -> R.raw.dsc_pro4
            is Fx05 -> R.raw.dsc_pro5
            is Fx06 -> R.raw.dsc_pro6
            is Fx07 -> R.raw.dsc_pro7
            is Fx08 -> R.raw.dsc_pro8
            is Fx09 -> R.raw.dsc_pro9
            is Fx10 -> R.raw.dsc_pro10
            is Fx11 -> R.raw.dsc_pro11
            is Fx12 -> R.raw.dsc_pro12
            is Fx13 -> R.raw.dsc_pro13
            is Fx14 -> R.raw.dsc_pro14
            is Fx15 -> R.raw.dsc_pro15
            is Fx16 -> R.raw.dsc_pro16
            is Fx17 -> R.raw.dsc_pro17
            is Fx18 -> R.raw.dsc_pro18
            is Fx19 -> R.raw.dsc_pro19
            is Fx20 -> R.raw.dsc_pro20
            is Fx21 -> R.raw.dsc_pro21
            is Fx22 -> R.raw.dsc_pro22
            is Fx23 -> R.raw.dsc_pro23
            is Fx24 -> R.raw.dsc_pro24
            is Fx25 -> R.raw.dsc_pro25
            is Fx26 -> R.raw.dsc_pro26
            is Fx27 -> R.raw.dsc_pro27
            is Fx28 -> R.raw.dsc_pro28
            is Fx29 -> R.raw.dsc_pro29
            is Fx30 -> R.raw.dsc_pro30
            is Fx31 -> R.raw.dsc_pro31
            is Fx32 -> R.raw.dsc_pro32
            is Fx33 -> R.raw.dsc_pro33
            is Fx34 -> R.raw.dsc_pro34
            is Fx35 -> R.raw.dsc_pro35
            is Fx36 -> R.raw.dsc_pro36
            is Fx37 -> R.raw.dsc_pro37
            is Fx38 -> R.raw.dsc_pro38
            is Fx39 -> R.raw.dsc_pro39
            is Fx40 -> R.raw.dsc_pro40
            is Fx41 -> R.raw.dsc_pro41
            is Fx42 -> R.raw.dsc_pro42
            is Fx43 -> R.raw.dsc_pro43
            is Fx44 -> R.raw.dsc_pro44
            is Fx45 -> R.raw.dsc_pro45
            is Fx46 -> R.raw.dsc_pro46
            is Fx47 -> R.raw.dsc_pro47
            is Fx48 -> R.raw.dsc_pro48
            is Fx49 -> R.raw.dsc_pro49
            is Fx50 -> R.raw.dsc_pro50
            is Fx51 -> R.raw.dsc_pro51
            is Fx52 -> R.raw.dsc_pro52
            is Fx53 -> R.raw.dsc_pro53
            is Fx54 -> R.raw.dsc_pro54
            is Fx55 -> R.raw.dsc_pro55
            is Fx56 -> R.raw.dsc_pro56
            is Fx57 -> R.raw.dsc_pro57
            is Fx58 -> R.raw.dsc_pro58
            is Fx59 -> R.raw.dsc_pro59
            is Fx60 -> R.raw.dsc_pro60
            is Fx61 -> R.raw.dsc_pro61
            is Fx62 -> R.raw.dsc_pro62
            is Fx63 -> R.raw.dsc_pro63
            is Fx64 -> R.raw.dsc_pro64
            is Fx65 -> R.raw.dsc_pro65
            is Fx66 -> R.raw.dsc_pro66
            is Fx67 -> R.raw.dsc_pro67
            is Fx68 -> R.raw.dsc_pro68
            is Fx69 -> R.raw.dsc_pro69
            is Fx70 -> R.raw.dsc_pro70
            is Fx71 -> R.raw.dsc_pro71
            is Fx72 -> R.raw.dsc_pro72
            is Fx73 -> R.raw.dsc_pro73
            is Fx74 -> R.raw.dsc_pro74
            is Fx75 -> R.raw.dsc_pro75
            is Fx76 -> R.raw.dsc_pro76
            is Fx77 -> R.raw.dsc_pro77
            is Fx78 -> R.raw.dsc_pro78
            is Fx79 -> R.raw.dsc_pro79
            is Fx80 -> R.raw.dsc_pro80
            is Fx81 -> R.raw.dsc_pro81
            is Fx82 -> R.raw.dsc_pro82
            is Fx83 -> R.raw.dsc_pro83
            is Fx84 -> R.raw.dsc_pro84
            is Fx85 -> R.raw.dsc_pro85
            is Fx86 -> R.raw.dsc_pro86
            is Fx87 -> R.raw.dsc_pro87
            is Fx88 -> R.raw.dsc_pro88
            is Fx89 -> R.raw.dsc_pro89
            is Fx90 -> R.raw.dsc_pro90
            is Fx91 -> R.raw.dsc_pro91
            is Fx92 -> R.raw.dsc_pro92
            is Fx93 -> R.raw.dsc_pro93
            is Fx94 -> R.raw.dsc_pro94
            is Fx95 -> R.raw.dsc_pro95
            is Fx96 -> R.raw.dsc_pro96
            is Fx97 -> R.raw.dsc_pro97
            is Fx98 -> R.raw.dsc_pro98
            is Fx99 -> R.raw.dsc_pro99
            is Fx100 -> R.raw.dsc_pro100
            is Fx101 -> R.raw.dsc_pro101
            is Fx102 -> R.raw.dsc_pro102
            is Fx103 -> R.raw.dsc_pro103
            is Fx104 -> R.raw.dsc_pro104
            is Fx105 -> R.raw.dsc_pro105
            is Fx106 -> R.raw.dsc_pro106
            is Fx107 -> R.raw.dsc_pro107
            is Fx108 -> R.raw.dsc_pro108
            is Fx109 -> R.raw.dsc_pro109
            is Fx110 -> R.raw.dsc_pro110
            is Fx111 -> R.raw.dsc_pro111
            is Fx112 -> R.raw.dsc_pro112
            is Fx113 -> R.raw.dsc_pro113
            is Fx114 -> R.raw.dsc_pro114
            is Fx115 -> R.raw.dsc_pro115
            is Fx116 -> R.raw.dsc_pro116
            is Fx117 -> R.raw.dsc_pro117
            is Fx118 -> R.raw.dsc_pro118
            is Fx119 -> R.raw.dsc_pro119
            is Fx120 -> R.raw.dsc_pro120
            is Fx121 -> R.raw.dsc_pro121
            is Fx122 -> R.raw.dsc_pro122
            is Fx123 -> R.raw.dsc_pro123
            is Fx124 -> R.raw.dsc_pro124
            is Fx125 -> R.raw.dsc_pro125
            is Fx126 -> R.raw.dsc_pro126
            is Fx127 -> R.raw.dsc_pro127
            is Fx128 -> R.raw.dsc_pro128
            is Fx129 -> R.raw.dsc_pro129
            is Fx130 -> R.raw.dsc_pro130
            is Fx131 -> R.raw.dsc_pro131
            is Fx132 -> R.raw.dsc_pro132
            is Fx133 -> R.raw.dsc_pro133
            is Fx134 -> R.raw.dsc_pro134
            is Fx135 -> R.raw.dsc_pro135
            is Fx136 -> R.raw.dsc_pro136
            is Fx137 -> R.raw.dsc_pro137
            is Fx138 -> R.raw.dsc_pro138
            is Fx139 -> R.raw.dsc_pro139
            is Fx140 -> R.raw.dsc_pro140
            is Fx141 -> R.raw.dsc_pro141
            is Fx142 -> R.raw.dsc_pro142
            is Fx143 -> R.raw.dsc_pro143
            is Fx144 -> R.raw.dsc_pro144
            is Fx145 -> R.raw.dsc_pro145
            is Fx146 -> R.raw.dsc_pro146
            is Fx147 -> R.raw.dsc_pro147
            is Fx148 -> R.raw.dsc_pro148
            is Fx149 -> R.raw.dsc_pro149
            is Fx150 -> R.raw.dsc_pro150
            is Fx151 -> R.raw.dsc_pro151
            is Fx152 -> R.raw.dsc_pro152
            is Fx153 -> R.raw.dsc_pro153
            is Fx154 -> R.raw.dsc_pro154
            is Fx155 -> R.raw.dsc_pro155
            is Fx156 -> R.raw.dsc_pro156
            is Fx157 -> R.raw.dsc_pro157
            is Fx158 -> R.raw.dsc_pro158
            is Fx159 -> R.raw.dsc_pro159
            is Fx160 -> R.raw.dsc_pro160
            is Fx161 -> R.raw.dsc_pro161
            is Fx162 -> R.raw.dsc_pro162
            is Fx163 -> R.raw.dsc_pro163
            is Fx164 -> R.raw.dsc_pro164
            is Fx165 -> R.raw.dsc_pro165
            is Fx166 -> R.raw.dsc_pro166
            is Fx167 -> R.raw.dsc_pro167
            is Fx168 -> R.raw.dsc_pro168
            is Fx169 -> R.raw.dsc_pro169
            is Fx170 -> R.raw.dsc_pro170
            is Fx171 -> R.raw.dsc_pro171
            is Fx174 -> R.raw.dsc_pro174
            is Fx177 -> R.raw.dsc_pro177
            is Fx180 -> R.raw.dsc_pro180
            else -> throw IllegalStateException("unknown sound $sound")
        }
    }
}