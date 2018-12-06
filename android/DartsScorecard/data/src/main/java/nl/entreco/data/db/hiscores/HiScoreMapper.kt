package nl.entreco.data.db.hiscores

import nl.entreco.data.db.player.PlayerTable
import nl.entreco.data.db.profile.ProfileTable
import nl.entreco.domain.hiscores.HiScore
import nl.entreco.domain.hiscores.HiScoreItem

class HiScoreMapper {
    fun to(player: PlayerTable, stats: List<ProfileTable>): HiScore {

        val aggregator = stats.sumBy { it.totalScore }
        val denominator = stats.sumBy { it.numDarts }

        val avg = when (denominator) {
            0 -> "--"
            else -> "%.2f".format(aggregator / denominator.toDouble() * 3)
        }

        val overallAverage = HiScoreItem.OverallAverage( avg )
        val num180s = HiScoreItem.Num180(stats.sumBy { it.num180s }.toString() )
        val num140s = HiScoreItem.Num140(stats.sumBy { it.num140s }.toString() )
        val num100s = HiScoreItem.Num100(stats.sumBy { it.num100s }.toString() )
        val num60s = HiScoreItem.Num60(stats.sumBy { it.num60s }.toString() )
        val hiscores = listOf(overallAverage, num180s, num140s, num100s, num60s)
        return HiScore(player.id, player.name, hiscores)
    }
}