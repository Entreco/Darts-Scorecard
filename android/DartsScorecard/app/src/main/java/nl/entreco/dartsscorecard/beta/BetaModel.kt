package nl.entreco.dartsscorecard.beta

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import nl.entreco.domain.beta.Feature

/**
 * Created by entreco on 30/01/2018.
 */
class BetaModel(val feature: Feature) {

    val total = format(feature.required)
    val votable = ObservableBoolean(feature.votes < feature.required)
    val title = ObservableField<String>(feature.title)
    val description = ObservableField<String>(feature.description)
    val goal = ObservableField<String>("${feature.votes} / $total")
    val progress = ObservableFloat(((feature.votes.toFloat() / feature.required.toFloat())))
    val image = ObservableField<String>(feature.image)
    val remarks = ObservableField<String>(formatHtml(feature.remarks))

    private fun format(value: Int): String {
        return when {
            value < 1000 -> "$value"
            else -> "${value / 1000}k"
        }
    }

    private fun formatHtml(remarks: String): String {
        return remarks
    }
}
