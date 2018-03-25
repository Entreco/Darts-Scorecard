package nl.entreco.dartsscorecard.play.stats

import android.view.View
import android.view.ViewPropertyAnimator
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding
import kotlin.math.max

/**
 * Created by entreco on 24/03/2018.
 */
class MatchStatAnimator(binding: WidgetListStatsBinding) {


    private var animator = MatchStatAnimatorHandler(binding.player1, binding.player2, binding.name1, binding.name2, binding.score, binding.stat1, binding.stat2, binding.stat3, binding.stat4, binding.stat5, binding.stat6, binding.stat7)

    fun transform(page: View, position: Float) {
        animator.transform(page, position)
    }

    fun onSlide(slideOffset: Float) {
        animator.slide(slideOffset)
    }

    internal class MatchStatAnimatorHandler(private val player1: View, private val player2: View,
                                            private val name1: View, private val name2: View, private val score: View,
                                            private val stat1: View, private val stat2: View, private val stat3: View,
                                            private val stat4: View, private val stat5: View, private val stat6: View,
                                            private val stat7: View) {

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
        }

        private fun animateState(anim: ViewPropertyAnimator, index: Int, slideOffset: Float) {
            anim.translationY(-index * 50 * slideOffset * index).scaleX(max(0f, (1 - slideOffset * index))).alpha(1 - slideOffset).setDuration(0).start()
        }

        // [-1. 1] range of position
        fun transform(page: View, position: Float) {

            page.animate().translationX(-position * page.width).setDuration(0).start()

            stayPut(score)
            animateRoll(player1, position, page.width)
            animateRoll(player2, position, page.width)


            animateFly(name1, position, page.width / 2F)
            animateFly(name2, position, page.width / 2F)

            animateFly(stat1, position, page.width * 1.10F)
            animateFly(stat2, position, page.width * 1.12F)
            animateFly(stat3, position, page.width * 1.20F)
            animateFly(stat4, position, page.width * 1.40F)
            animateFly(stat5, position, page.width * 1.80F)
            animateFly(stat6, position, page.width * 2.00F)
            animateFly(stat7, position, page.width * 3.00F)
        }

        private fun stayPut(view: View) {
            view.z = 100F
            view.animate().translationX(0F).setDuration(0).start()
        }

        private fun animateFly(view: View, position: Float, factor: Float) {
            view.animate().translationX(position * factor).setDuration(0).start()
        }

        private fun animateRoll(view: View, position: Float, pageWidth: Int) {
            val x = position * (view.width + ((pageWidth - 2*view.width)/3))
            view.animate().translationX(x).setDuration(0).start()
        }
    }
}