package nl.entreco.data.db.profile

import nl.entreco.data.db.meta.MetaTable
import nl.entreco.data.db.turn.TurnTable
import nl.entreco.domain.model.players.PlayerSeperator


class ArchiveStatMapper {
    fun to(gameId: Long, playerId: Long, winningTeam: String, turns: List<TurnTable>, metas: List<MetaTable>): ProfileTable {
        val table = ProfileTable()
        table.gameId = gameId
        table.playerId = playerId

        val playerTurnMetaPairs = turns.zip(metas).filter { it.first.player == playerId && it.second.playerId == playerId }
        val scores = playerTurnMetaPairs.map { score(it.first) }

        countAverages(table, playerTurnMetaPairs, scores)
        countScoreFrequencies(table, scores)
        countCheckOuts(table, playerTurnMetaPairs)

        table.didWin = winningTeam.split(PlayerSeperator).map { it.toLong() }.contains(playerId)

        return table
    }

    private fun countAverages(table: ProfileTable, playerTurnMetaPairs: List<Pair<TurnTable, MetaTable>>, scores: List<Int>) {
        table.numDarts = playerTurnMetaPairs.sumOf { it.first.numDarts }
        table.totalScore = scores.sumOf { it }
        table.numDarts9 = playerTurnMetaPairs.filter { it.second.turnInLeg <= 3 }.sumOf { it.first.numDarts }
        table.totalScore9 = playerTurnMetaPairs.filter { it.second.turnInLeg <= 3 }.sumOf { score(it.first) }
    }

    private fun countScoreFrequencies(table: ProfileTable, scores: List<Int>) {
        table.num180s = scores.count { it == 180 }
        table.num140s = scores.count { it in 140..179 }
        table.num100s = scores.count { it in 100..139 }
        table.num60s = scores.count { it in 60..99 }
        table.num20s = scores.count { it in 20..59 }
        table.num0s = scores.count { it == 0 }
    }

    private fun countCheckOuts(table: ProfileTable, playerTurnMetaPairs: List<Pair<TurnTable, MetaTable>>) {
        table.numDartsAtFinish = playerTurnMetaPairs.sumOf { it.second.atCheckout }
        table.numFinishes = playerTurnMetaPairs.count { it.second.score == score(it.first) }
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