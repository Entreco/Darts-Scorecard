package nl.entreco.dartsscorecard.profile.view

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import nl.entreco.domain.profile.ProfileStat


class PlayerStats(stat: ProfileStat) {
    val isEmpty = ObservableBoolean(stat.numberOfGames <= 0)
    val gamesPlayed = ObservableField<String>("${stat.numberOfGames}")
    val average = ObservableField<String>("%.2f".format(avgOf(stat.numberOfPoints, stat.numberOfDarts) * 3F))
    val average9 = ObservableField<String>("%.2f".format(avgOf(stat.numberOfPoints9, stat.numberOfDarts9) * 3F))
    val thrown = ObservableField<String>("${stat.numberOfDarts}")
    val winRatio = ObservableField<String>("${stat.numberOfWins}/${stat.numberOfGames}")
    val checkout = ObservableField<String>("${stat.numberOfFinishes}/${stat.numberOfDartsAtFinish}")
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
}