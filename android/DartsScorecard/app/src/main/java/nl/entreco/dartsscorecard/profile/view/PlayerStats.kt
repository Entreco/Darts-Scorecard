package nl.entreco.dartsscorecard.profile.view

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import nl.entreco.domain.profile.ProfileStat


class PlayerStats(stat: ProfileStat) {
    val isEmpty = ObservableBoolean(stat.numberOfGames <= 0)
    val gamesPlayed = ObservableField<String>("${stat.numberOfGames}")
    val average = ObservableField<String>("%.2f".format(avgOf(stat.numberOfPoints, stat.numberOfDarts) * 3F))
    val average9 = ObservableField<String>("%.2f".format(avgOf(stat.numberOfPoints9, stat.numberOfDarts9) * 3F))
    val winPercentage = ObservableField<String>(percentage(stat.numberOfWins, stat.numberOfGames))
    val checkoutPercentage = ObservableField<String>(percentage(stat.numberOfFinishes, stat.numberOfDartsAtFinish))
    val num180s = ObservableField<String>("${stat.numberOf180s}")
    val num140s = ObservableField<String>("${stat.numberOf140s}")
    val num100s = ObservableField<String>("${stat.numberOf100s}")
    val num60s = ObservableField<String>("${stat.numberOf60s}")
    val num20s = ObservableField<String>("${stat.numberOf20s}")
    val num0s = ObservableField<String>("${stat.numberOf0s}")

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