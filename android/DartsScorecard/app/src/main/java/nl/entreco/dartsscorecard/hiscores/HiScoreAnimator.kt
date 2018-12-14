package nl.entreco.dartsscorecard.hiscores

import android.view.View
import nl.entreco.dartsscorecard.databinding.ActivityHiscoreBinding

class HiScoreAnimator(val binding: ActivityHiscoreBinding) {
    private val pager = binding.hiscorePager
    private val prev = binding.hiscorePrev.also {
        flyOutStart(it)
    }

    private val next = binding.hiscoreNext.also {
        flyOutEnd(it)
    }

    fun onPageSelected(position: Int) {
        when {
            position <= 0 -> flyOutStart(prev)
            position + 1 >= pager.adapter?.count ?: 0 -> flyOutEnd(next)
            else -> {
                flyInStart(prev)
                flyInEnd(next)
            }
        }
    }

    private fun flyOutStart(view: View) {
        view.animate().alpha(0F).translationX(-view.width.toFloat()).scaleX(0F).scaleY(0F).start()
    }

    private fun flyInStart(view: View) {
        view.animate().alpha(1F).translationX(0F).scaleX(1F).scaleY(1F).start()
    }

    private fun flyOutEnd(view: View) {
        view.animate().alpha(0F).translationX(view.width.toFloat()).scaleX(0F).scaleY(0F).start()
    }

    private fun flyInEnd(view: View) {
        view.animate().alpha(1F).translationX(0F).scaleX(1F).scaleY(1F).start()
    }
}