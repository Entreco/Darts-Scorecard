package nl.entreco.dartsscorecard.profile.view

import android.databinding.ObservableInt
import nl.entreco.domain.profile.ProfileStat


class PlayerStats(stat: ProfileStat) {
    val gamesPlayed = ObservableInt(stat.numberOfGames)
}