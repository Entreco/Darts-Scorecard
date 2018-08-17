package nl.entreco.dartsscorecard.tv.match

import android.databinding.ObservableField
import android.databinding.ObservableInt

class MatchViewModel {

    val numSets = ObservableInt(3)
    val description = ObservableField<String>("This is description on the TV")
}