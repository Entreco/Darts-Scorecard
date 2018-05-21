package nl.entreco.data.db.profile

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnTable


class ArchiveStatMapper {
    fun to(gameId: Long, playerId: Long, turns: List<TurnTable>, metas: List<MetaTable>): ProfileTable {
        val table = ProfileTable()
        table.gameId = gameId
        table.playerId = playerId
        table.numDarts = turns.filter { it.player == playerId }.sumBy { it.numDarts }
        table.totalScore = turns.filter { it.player == playerId }.sumBy { score(it) }
        return table
    }

    private fun score(it: TurnTable): Int {
        return when (it.numDarts) {
            0 -> 0
            1 -> it.d1 * it.m1
            2 -> it.d1 * it.m1 + it.d2 * it.m2
            3 -> it.d1 * it.m1 + it.d2 * it.m2 + it.d3 * it.m3
            else -> 0
        }
    }
}