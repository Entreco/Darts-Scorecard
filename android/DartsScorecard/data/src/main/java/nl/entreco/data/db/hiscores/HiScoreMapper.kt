package nl.entreco.data.db.hiscores

import nl.entreco.data.db.player.PlayerTable
import nl.entreco.data.db.profile.ProfileTable
import nl.entreco.domain.hiscores.HiScore
import nl.entreco.domain.hiscores.HiScoreItem

class HiScoreMapper {
    fun to(player: PlayerTable, stats: List<ProfileTable>): HiScore {

        val numberOfMatches = stats.size
        val numberOfGamesWon = stats.sumOf { (if (it.didWin) 1 else 0).toInt() }
        val avg: Float = avg(stats.sumOf { it.numDarts }, stats.sumOf { it.totalScore })
        val firstNineAvg: Float = avg(stats.sumOf { it.numDarts9 }, stats.sumOf { it.totalScore9 })
        val checkOuts: Float = avg(stats.sumOf { it.numDartsAtFinish }, stats.sumOf { it.numFinishes })
        val scores = listOf(
                HiScoreItem.OverallAvg(avg * 3),
                HiScoreItem.ScoringAvg(firstNineAvg * 3),
                HiScoreItem.CheckoutPerc(checkOuts * 100),
                HiScoreItem.WinRatio(avg(numberOfMatches, numberOfGamesWon) * 100 + numberOfGamesWon, numberOfMatches, numberOfGamesWon),
                HiScoreItem.Num180(stats.sumOf { it.num180s }),
                HiScoreItem.Num140(stats.sumOf { it.num140s }),
                HiScoreItem.Num100(stats.sumOf { it.num100s }),
                HiScoreItem.Num60(stats.sumOf { it.num60s }),
                HiScoreItem.Num20(stats.sumOf { it.num20s }),
                HiScoreItem.NumBust(stats.sumOf { it.num0s }),
                HiScoreItem.BestMatchAvg(stats.map { avg(it.numDarts, it.totalScore) * 3}.maxOrNull() ?: 0F),
                HiScoreItem.BestMatchCheckout(stats.map { avg(it.numDartsAtFinish, it.numFinishes) * 100}.maxOrNull() ?: 0F)
        )
        return HiScore(player.id, player.name, scores)
    }

    private fun avg(denominator: Int, aggregator: Int): Float {
        return when (denominator) {
            0 -> 0.0F
            else -> aggregator.toFloat() / denominator.toFloat()
        }
    }
}