package nl.entreco.dartsscorecard.hiscores

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

data class HiScoreItemModel(val id: Long, val name: String, val hiScore: String, val pos: Int) {
    val score = ObservableField(hiScore)
    val position = ObservableField("$pos")
    val hasMedal = ObservableBoolean(pos <= 3)
}