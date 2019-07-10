package nl.entreco.dartsscorecard.profile.view

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import nl.entreco.domain.profile.ProfileStat


class PlayerStats(stat: ProfileStat) {
    val isEmpty = ObservableBoolean(stat.numberOfGames <= 0)
    val gamesPlayed = ObservableField("${stat.numberOfGames}")
    val average = ObservableField("%.2f".format(avgOf(stat.numberOfPoints, stat.numberOfDarts) * 3F))
    val average9 = ObservableField("%.2f".format(avgOf(stat.numberOfPoints9, stat.numberOfDarts9) * 3F))
    val winPercentage = ObservableField(percentage(stat.numberOfWins, stat.numberOfGames))
    val checkoutPercentage = ObservableField(percentage(stat.numberOfFinishes, stat.numberOfDartsAtFinish))
    val num180s = ObservableField("${stat.numberOf180s}")
    val num140s = ObservableField("${stat.numberOf140s}")
    val num100s = ObservableField("${stat.numberOf100s}")
    val num60s = ObservableField("${stat.numberOf60s}")
    val num20s = ObservableField("${stat.numberOf20s}")
    val num0s = ObservableField("${stat.numberOf0s}")

    private fun avgOf(sum: Int, total: Int): Float = when (total) {
        0 -> 0F
        else -> sum / total.toFloat()
    }

    private fun percentage(amount: Int, total: Int): String {
        return when(total){
            0 -> "--"
            else -> "%.2f".format(amount / total.toFloat() * 100.0) + "%"
        }
    }
}