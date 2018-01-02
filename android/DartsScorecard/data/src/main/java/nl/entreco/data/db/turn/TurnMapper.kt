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
            1 -> Turn(Dart.fromInt(from.d1, from.m1))
            2 -> Turn(Dart.fromInt(from.d1, from.m1), Dart.fromInt(from.d2, from.m2))
            3 -> Turn(Dart.fromInt(from.d1, from.m1), Dart.fromInt(from.d2, from.m2), Dart.fromInt(from.d3, from.m3))
            else -> Turn()
        }
    }

    fun from(gameId: Long, turn: Turn): TurnTable {
        val table = TurnTable()
        table.game = gameId
        table.d1 = turn.first().number()
        table.m1 = turn.first().multiplier()
        table.d2 = turn.second().number()
        table.m2 = turn.second().multiplier()
        table.d3 = turn.third().number()
        table.m3 = turn.third().multiplier()
        table.numDarts = 3 - turn.dartsLeft()
        return table
    }
}