package nl.entreco.dartsscorecard.profile.view

import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import nl.entreco.domain.profile.ProfileStat


class PlayerStats(stat: ProfileStat) {
    val isEmpty = ObservableBoolean(stat.numberOfGames <= 0)
    val gamesPlayed = ObservableInt(stat.numberOfGames)
}