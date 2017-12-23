package nl.entreco.data.db.turn

import nl.entreco.data.db.Mapper
import nl.entreco.domain.model.Dart
import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 23/12/2017.
 */
class TurnMapper : Mapper<TurnTable, Turn> {
    override fun to(from: TurnTable): Turn {
        return when (from.numDarts) {
            1 -> Turn(Dart.fromInt(from.d1))
            2 -> Turn(Dart.fromInt(from.d1), Dart.fromInt(from.d2))
            3 -> Turn(Dart.fromInt(from.d1), Dart.fromInt(from.d2), Dart.fromInt(from.d3))
            else -> Turn()
        }
    }
}