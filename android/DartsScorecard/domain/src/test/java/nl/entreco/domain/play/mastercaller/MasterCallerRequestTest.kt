package nl.entreco.domain.play.mastercaller

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCallerRequestTest{

    @Test
    fun `it should map to 'Fx00' when scoring 0`() {
        assertTrue( req(0) is Fx00)
    }

    @Test
    fun `it should map to 'Fx01' when scoring 1`() {
        assertTrue( req(1) is Fx01)
    }

    @Test
    fun `it should map to 'Fx02' when scoring 2`() {
        assertTrue( req(2) is Fx02)
    }

    @Test
    fun `it should map to 'others' when scoring others`() {
        assertTrue(req(3) is Fx03 )
        assertTrue(req(4) is Fx04 )
        assertTrue(req(5) is Fx05 )
        assertTrue(req(6) is Fx06 )
        assertTrue(req(7) is Fx07 )
        assertTrue(req(8) is Fx08 )
        assertTrue(req(9) is Fx09 )
        assertTrue(req(10) is Fx10 )
        assertTrue(req(11) is Fx11 )
        assertTrue(req(12) is Fx12 )
        assertTrue(req(13) is Fx13 )
        assertTrue(req(14) is Fx14 )
        assertTrue(req(15) is Fx15 )
        assertTrue(req(16) is Fx16 )
        assertTrue(req(17) is Fx17 )
        assertTrue(req(18) is Fx18 )
        assertTrue(req(19) is Fx19 )
        assertTrue(req(20) is Fx20 )
        assertTrue(req(21) is Fx21 )
        assertTrue(req(22) is Fx22 )
        assertTrue(req(23) is Fx23 )
        assertTrue(req(24) is Fx24 )
        assertTrue(req(25) is Fx25 )
        assertTrue(req(26) is Fx26 )
        assertTrue(req(27) is Fx27 )
        assertTrue(req(28) is Fx28 )
        assertTrue(req(29) is Fx29 )
        assertTrue(req(30) is Fx30 )
        assertTrue(req(31) is Fx31 )
        assertTrue(req(32) is Fx32 )
        assertTrue(req(33) is Fx33 )
        assertTrue(req(34) is Fx34 )
        assertTrue(req(35) is Fx35 )
        assertTrue(req(36) is Fx36 )
        assertTrue(req(37) is Fx37 )
        assertTrue(req(38) is Fx38 )
        assertTrue(req(39) is Fx39 )
        assertTrue(req(40) is Fx40 )
        assertTrue(req(41) is Fx41 )
        assertTrue(req(42) is Fx42 )
        assertTrue(req(43) is Fx43 )
        assertTrue(req(44) is Fx44 )
        assertTrue(req(45) is Fx45 )
        assertTrue(req(46) is Fx46 )
        assertTrue(req(47) is Fx47 )
        assertTrue(req(48) is Fx48 )
        assertTrue(req(49) is Fx49 )
        assertTrue(req(50) is Fx50 )
        assertTrue(req(51) is Fx51 )
        assertTrue(req(52) is Fx52 )
        assertTrue(req(53) is Fx53 )
        assertTrue(req(54) is Fx54 )
        assertTrue(req(55) is Fx55 )
        assertTrue(req(56) is Fx56 )
        assertTrue(req(57) is Fx57 )
        assertTrue(req(58) is Fx58 )
        assertTrue(req(59) is Fx59 )
        assertTrue(req(60) is Fx60 )
        assertTrue(req(61) is Fx61 )
        assertTrue(req(62) is Fx62 )
        assertTrue(req(63) is Fx63 )
        assertTrue(req(64) is Fx64 )
        assertTrue(req(65) is Fx65 )
        assertTrue(req(66) is Fx66 )
        assertTrue(req(67) is Fx67 )
        assertTrue(req(68) is Fx68 )
        assertTrue(req(69) is Fx69 )
        assertTrue(req(70) is Fx70 )
        assertTrue(req(71) is Fx71 )
        assertTrue(req(72) is Fx72 )
        assertTrue(req(73) is Fx73 )
        assertTrue(req(74) is Fx74 )
        assertTrue(req(75) is Fx75 )
        assertTrue(req(76) is Fx76 )
        assertTrue(req(77) is Fx77 )
        assertTrue(req(78) is Fx78 )
        assertTrue(req(79) is Fx79 )
        assertTrue(req(80) is Fx80 )
        assertTrue(req(81) is Fx81 )
        assertTrue(req(82) is Fx82 )
        assertTrue(req(83) is Fx83 )
        assertTrue(req(84) is Fx84 )
        assertTrue(req(85) is Fx85 )
        assertTrue(req(86) is Fx86 )
        assertTrue(req(87) is Fx87 )
        assertTrue(req(88) is Fx88 )
        assertTrue(req(89) is Fx89 )
        assertTrue(req(90) is Fx90 )
        assertTrue(req(91) is Fx91 )
        assertTrue(req(92) is Fx92 )
        assertTrue(req(93) is Fx93 )
        assertTrue(req(94) is Fx94 )
        assertTrue(req(95) is Fx95 )
        assertTrue(req(96) is Fx96 )
        assertTrue(req(97) is Fx97 )
        assertTrue(req(98) is Fx98 )
        assertTrue(req(99) is Fx99 )
        assertTrue(req(100) is Fx100 )
        assertTrue(req(101) is Fx101 )
        assertTrue(req(102) is Fx102 )
        assertTrue(req(103) is Fx103 )
        assertTrue(req(104) is Fx104 )
        assertTrue(req(105) is Fx105 )
        assertTrue(req(106) is Fx106 )
        assertTrue(req(107) is Fx107 )
        assertTrue(req(108) is Fx108 )
        assertTrue(req(109) is Fx109 )
        assertTrue(req(110) is Fx110 )
        assertTrue(req(111) is Fx111 )
        assertTrue(req(112) is Fx112 )
        assertTrue(req(113) is Fx113 )
        assertTrue(req(114) is Fx114 )
        assertTrue(req(115) is Fx115 )
        assertTrue(req(116) is Fx116 )
        assertTrue(req(117) is Fx117 )
        assertTrue(req(118) is Fx118 )
        assertTrue(req(119) is Fx119 )
        assertTrue(req(120) is Fx120 )
        assertTrue(req(121) is Fx121 )
        assertTrue(req(122) is Fx122 )
        assertTrue(req(123) is Fx123 )
        assertTrue(req(124) is Fx124 )
        assertTrue(req(125) is Fx125 )
        assertTrue(req(126) is Fx126 )
        assertTrue(req(127) is Fx127 )
        assertTrue(req(128) is Fx128 )
        assertTrue(req(129) is Fx129 )
        assertTrue(req(130) is Fx130 )
        assertTrue(req(131) is Fx131 )
        assertTrue(req(132) is Fx132 )
        assertTrue(req(133) is Fx133 )
        assertTrue(req(134) is Fx134 )
        assertTrue(req(135) is Fx135 )
        assertTrue(req(136) is Fx136 )
        assertTrue(req(137) is Fx137 )
        assertTrue(req(138) is Fx138 )
        assertTrue(req(139) is Fx139 )
        assertTrue(req(140) is Fx140 )
        assertTrue(req(141) is Fx141 )
        assertTrue(req(142) is Fx142 )
        assertTrue(req(143) is Fx143 )
        assertTrue(req(144) is Fx144 )
        assertTrue(req(145) is Fx145 )
        assertTrue(req(146) is Fx146 )
        assertTrue(req(147) is Fx147 )
        assertTrue(req(148) is Fx148 )
        assertTrue(req(149) is Fx149 )
        assertTrue(req(150) is Fx150 )
        assertTrue(req(151) is Fx151 )
        assertTrue(req(152) is Fx152 )
        assertTrue(req(153) is Fx153 )
        assertTrue(req(154) is Fx154 )
        assertTrue(req(155) is Fx155 )
        assertTrue(req(156) is Fx156 )
        assertTrue(req(157) is Fx157 )
        assertTrue(req(158) is Fx158 )
        assertTrue(req(159) is Fx159 )
        assertTrue(req(160) is Fx160 )
        assertTrue(req(161) is Fx161 )
        assertTrue(req(162) is Fx162 )
        assertTrue(req(163) is Fx163 )
        assertTrue(req(164) is Fx164 )
        assertTrue(req(165) is Fx165 )
        assertTrue(req(166) is Fx166 )
        assertTrue(req(167) is Fx167 )
        assertTrue(req(168) is Fx168 )
        assertTrue(req(169) is Fx169 )
        assertTrue(req(170) is Fx170 )
        assertTrue(req(171) is Fx171 )
        assertTrue(req(174) is Fx174 )
        assertTrue(req(177) is Fx177 )
        assertTrue(req(180) is Fx180 )
    }



    @Test
    fun `it should map to 'None' for unknown scores`() {
        assertTrue(req(181) is None)
    }

    private fun req(scored: Int) = MasterCallerRequest(scored = scored).toSound()
}