package nl.entreco.dartsscorecard.play.live

import android.databinding.DataBindingUtil
import android.view.View
import android.view.ViewPropertyAnimator
import nl.entreco.dartsscorecard.R.id.stat11
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding
import kotlin.math.max

/**
 * Created by entreco on 24/03/2018.
 */
class LiveStatSlideAnimator(private val view: View, private val left: View?, private val right: View?) {

    private val binding by lazy { DataBindingUtil.getBinding<WidgetListStatsBinding>(view)!! }
    private val animator by lazy {
        MatchStatSlideAnimatorHandler(binding.player1, binding.player2, binding.name1, binding.name2, binding.score,
                binding.stat1, binding.stat2, binding.stat3, binding.stat4, binding.stat5, binding.stat6, binding.stat7,
                binding.stat8, binding.stat9, binding.stat10, binding.stat11)
    }

    fun onSlide(slideOffset: Float) {
        view.alpha = 1F
        left?.alpha = if (slideOffset > 0) 0F else 1F
        right?.alpha = if (slideOffset > 0) 0F else 1F
        animator.slide(slideOffset)
    }

    internal class MatchStatSlideAnimatorHandler(private val player1: View, private val player2: View,
                                                 private val name1: View, private val name2: View, private val score: View,
                                                 private val stat1: View, private val stat2: View, private val stat3: View,
                                                 private val stat4: View, private val stat5: View, private val stat6: View,
                                                 private val stat7: View, private val stat8: View, private val stat9: View,
                                                 private val stat10: View, private val stat11: View) {
        fun slide(slideOffset: Float) {
            // Fly In Players
            player1.animate().translationX(slideOffset * -player1.width / 3).setDuration(0).start()
            player2.animate().translationX(slideOffset * player2.width / 3).setDuration(0).start()
            name1.animate().translationX(slideOffset * -name1.width).setDuration(0).start()
            name2.animate().translationX(slideOffset * name2.width).setDuration(0).start()
            score.animate().alpha(1 - slideOffset).scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()

            // Fly In stats
            animateState(stat1.animate(), 1, slideOffset)
            animateState(stat2.animate(), 2, slideOffset)
            animateState(stat3.animate(), 3, slideOffset)
            animateState(stat4.animate(), 4, slideOffset)
            animateState(stat5.animate(), 5, slideOffset)
            animateState(stat6.animate(), 6, slideOffset)
            animateState(stat7.animate(), 7, slideOffset)
            animateState(stat8.animate(), 8, slideOffset)
            animateState(stat9.animate(), 9, slideOffset)
            animateState(stat10.animate(), 10, slideOffset)
            animateState(stat11.animate(), 11, slideOffset)
        }

        private fun animateState(anim: ViewPropertyAnimator, index: Int, slideOffset: Float) {
            anim.translationY(-index * 50 * slideOffset * index).scaleX(max(0f, (1 - slideOffset * index))).alpha(1 - slideOffset).setDuration(0).start()
        }
    }
}