package nl.entreco.domain.play.listeners

import nl.entreco.domain.play.model.Dart
import nl.entreco.domain.play.model.Turn

/**
 * Created by Entreco on 05/12/2017.
 */
abstract class SpecialEventListenerTest {
    fun `180`(): Turn = Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20)
    fun `60`(): Turn = Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20)
    fun `No Score`(): Turn = Turn(Dart.ZERO, Dart.ZERO, Dart.ZERO)
}