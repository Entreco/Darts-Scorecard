package nl.entreco.dartsscorecard.beta

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import nl.entreco.domain.beta.Feature

/**
 * Created by entreco on 30/01/2018.
 */
class BetaModel(val feature: Feature) {

    val total = format(feature.required)
    val votable = ObservableBoolean(feature.votes < feature.required)
    val title = ObservableField(feature.title)
    val description = ObservableField(feature.description)
    val goal = ObservableField("${feature.votes} / $total")
    val progress = ObservableFloat(((feature.votes.toFloat() / feature.required.toFloat())))
    val image = ObservableField(feature.image)
    val remarks = ObservableField(formatHtml(feature.remarks))
    val video = ObservableField(feature.video)

    private fun format(value: Int): String {
        return when {
            value < 1000 -> "$value"
            else -> "%.1f".format(value / 1000.0).removeSuffix(".0") + "k"
        }
    }

    private fun formatHtml(remarks: String): String {
        return remarks
    }
}
