package nl.entreco.dartsscorecard.play.stats

import android.view.View
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding

/**
 * Created by entreco on 27/03/2018.
 */
class MatchStatPageAnimator(private val size: Float) {

    fun transform(binding: WidgetListStatsBinding, page: View, position: Float) {
        page.alpha = 1F

        val statFactor = page.width - size
        val animator = MatchStatPageAnimatorHandler(binding.player1, binding.player2, binding.name1, binding.name2, binding.score,
                binding.stat1, binding.stat2, binding.stat3, binding.stat4, binding.stat5, binding.stat6, binding.stat7, statFactor)
        animator.transform(page, position)
    }

    internal class MatchStatPageAnimatorHandler(private val player1: View, private val player2: View,
                                                private val name1: View, private val name2: View, private val score: View,
                                                private val stat1: View, private val stat2: View, private val stat3: View,
                                                private val stat4: View, private val stat5: View, private val stat6: View,
                                                private val stat7: View, private val statFactor: Float) {

        // [-1. 1] range of position
        fun transform(page: View, position: Float) {

            page.animate().translationX(-position * page.width).setDuration(0).start()
            if (position <= 0.5) {
                page.z = 160F
            } else {
                page.z = 0F
            }

            stayPut(score)
            animateRoll(player1, position, page.width)
            animateRoll(player2, position, page.width)

            animateFly(name1, position, page.width / 2F)
            animateFly(name2, position, page.width / 2F)

            animateStat(stat1, position, statFactor)
            animateStat(stat2, position, statFactor)
            animateStat(stat3, position, statFactor)
            animateStat(stat4, position, statFactor)
            animateStat(stat5, position, statFactor)
            animateStat(stat6, position, statFactor)
            animateStat(stat7, position, statFactor)
        }

        private fun stayPut(view: View) {
            view.z = 100F
            view.animate().translationX(0F).setDuration(0).start()
        }

        private fun animateRoll(view: View, position: Float, pageWidth: Int) {
            val x = position * (view.width + ((pageWidth - 2 * view.width) / 3))
            view.animate().translationX(x).setDuration(0).start()
        }

        private fun animateFly(view: View, position: Float, factor: Float) {
            view.animate().translationX(position * factor).setDuration(0).start()
        }

        private fun animateStat(view: View, position: Float, factor: Float) {
//            val x: Float = factor * cos((position - 1) * 0.5 * Math.PI).toFloat()
            val x: Float = factor * position
            view.animate().translationX(x).setDuration(0).start()
        }
    }
}