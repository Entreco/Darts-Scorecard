package nl.entreco.data.db.profile

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.players.PlayerSeperator


class ArchiveStatMapper {
    fun to(gameId: Long, playerId: Long, winningTeam: String, turns: List<TurnTable>, metas: List<MetaTable>): ProfileTable {
        val table = ProfileTable()
        table.gameId = gameId
        table.playerId = playerId

        turns.filter { it.player == playerId }.also {
            table.numDarts = it.sumBy { it.numDarts }
            table.totalScore = it.sumBy { score(it) }
            table.num180s = it.count { score(it) == 180 }
            table.num140s = it.count { score(it) == 140 }
            table.num100s = it.count { score(it) == 100 }
            table.num60s = it.count { score(it) == 60 }
            table.num20s = it.count { score(it) == 20 }
            table.num0s = it.count { score(it) == 0 }
        }

        table.didWin = winningTeam.split(PlayerSeperator).map { it.toLong() }.contains(playerId)

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