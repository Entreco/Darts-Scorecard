package nl.entreco.dartsscorecard.hiscores

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import nl.entreco.domain.hiscores.HiScoreItem

data class HiScoreItemModel(val name: String, val hiScore: String, val pos: Int){
    val score = ObservableField<String>(hiScore)
    val position = ObservableField<String>("$pos")
    val hasMedal = ObservableBoolean(pos <= 3)
}