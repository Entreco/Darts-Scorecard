package nl.entreco.domain.play.listeners

import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 05/12/2017.
 */
abstract class SpecialEventListenerTest {
    fun `180`(): Turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20)
    fun `177`(): Turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_19)
    fun `171`(): Turn = Turn(Dart.TRIPLE_19, Dart.TRIPLE_19, Dart.TRIPLE_19)
    fun `167`(): Turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_19, Dart.BULL)
    fun `60`(): Turn = Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20)
    fun `No Score`(): Turn = Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO)
    fun `bust`(): Turn = Turn(Dart.TEST_501, Dart.TEST_501, Dart.TEST_501)
}