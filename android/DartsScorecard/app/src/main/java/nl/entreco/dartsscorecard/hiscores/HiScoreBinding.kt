package nl.entreco.dartsscorecard.hiscores

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import nl.entreco.dartsscorecard.R

object HiScoreBinding {

    @JvmStatic
    @BindingAdapter("medal")
    fun showMedalForPosition(view: ImageView, position: Int) {
        val (_, medal) = when (position) {
            1 -> Pair(2, R.drawable.medal_1)
            2 -> Pair(2, R.drawable.medal_2)
            3 -> Pair(2, R.drawable.medal_3)
            else -> Pair(0, 0)
        }
        view.setImageResource(medal)
    }
}