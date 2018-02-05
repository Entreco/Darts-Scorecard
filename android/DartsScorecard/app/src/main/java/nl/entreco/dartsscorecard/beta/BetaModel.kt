package nl.entreco.dartsscorecard.beta

import android.databinding.ObservableField
import android.databinding.ObservableFloat
import nl.entreco.domain.beta.Feature

/**
 * Created by entreco on 30/01/2018.
 */
class BetaModel(feature: Feature) {

    private val count = format(feature.votes)
    private val total = format(feature.required)

    val title = ObservableField<String>(feature.title)
    val description = ObservableField<String>(feature.description)
    val goal = ObservableField<String>("$count / $total")
    val progress = ObservableFloat(((feature.votes.toFloat() / feature.required.toFloat())))
    val image = ObservableField<String>(feature.image)

    private fun format(value: Int): String {
        return when{
            value < 1000 -> "$value"
            else -> "${value / 1000}k"
        }
    }
}