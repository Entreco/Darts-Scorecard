package nl.entreco.dartsscorecard.profile.view

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import nl.entreco.domain.profile.ProfileStat


class PlayerStats(stat: ProfileStat) {
    val isEmpty = ObservableBoolean(stat.numberOfGames <= 0)
    val gamesPlayed = ObservableField<String>("${stat.numberOfGames}")
    val average = ObservableField<String>("%.2f".format(avgOf(stat.numberOfPoints, stat.numberOfDarts) * 3F))

    private fun avgOf(sum: Int, total: Int) : Float = when(total){
        0 -> 0F
        else ->  sum / total.toFloat()
    }
}