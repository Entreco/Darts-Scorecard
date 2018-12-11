package nl.entreco.data.db.hiscores

import nl.entreco.data.db.player.PlayerTable
import nl.entreco.data.db.profile.ProfileTable
import nl.entreco.domain.hiscores.HiScore
import nl.entreco.domain.hiscores.HiScoreItem

class HiScoreMapper {
    fun to(player: PlayerTable, stats: List<ProfileTable>): HiScore {

        val avg: Float = avg(stats.sumBy { it.numDarts }, stats.sumBy { it.totalScore })
        val firstNineAvg: Float = avg(stats.sumBy { it.numDarts9 }, stats.sumBy { it.totalScore9 })
        val scores = listOf(
            HiScoreItem.OverallAverage(avg),
            HiScoreItem.FirstNineAvg(firstNineAvg),
            HiScoreItem.Num180(stats.sumBy { it.num180s }),
            HiScoreItem.Num140(stats.sumBy { it.num140s }),
            HiScoreItem.Num100(stats.sumBy { it.num100s }),
            HiScoreItem.Num60(stats.sumBy { it.num60s }),
            HiScoreItem.Num20(stats.sumBy { it.num20s }),
            HiScoreItem.NumBust(stats.sumBy { it.num0s })
        )
        return HiScore(player.id, player.name, scores)
    }

    private fun avg(denominator: Int, aggregator: Int): Float {
        return when (denominator) {
            0 -> 0.0F
            else -> (aggregator.toFloat() / denominator.toFloat() * 3F)
        }
    }
}