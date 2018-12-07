package nl.entreco.dartsscorecard.hiscores

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import nl.entreco.dartsscorecard.R

object HiScoreBinding {

    @JvmStatic
    @BindingAdapter("medal")
    fun showMedalForPosition(view: ImageView, position: String){
        val medal = when(position){
            "0" -> R.drawable.ic_medal_gold
            "1" -> R.drawable.ic_medal_silver
            "2" -> R.drawable.ic_medal_bronze
            else -> 0
        }
        view.setImageResource(medal)
    }
}