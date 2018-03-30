package nl.entreco.dartsscorecard.launch

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import nl.entreco.dartsscorecard.databinding.ActivityLaunchBinding

/**
 * Created by Entreco on 19/12/2017.
 */
class LaunchAnimator(binding: ActivityLaunchBinding) {

    private val animator = LaunchAnimatorHandler(binding.includeLaunchHeader.lets, binding.includeLaunchHeader.play,
            binding.includeLaunchHeader.dart, binding.launchResume, binding.launchPlay, binding.launchPlayers, binding.launchBeta)

    init {
        animator.init()
    }

    internal class LaunchAnimatorHandler(private val lets: View, private val play: View, private val darts: View,
                                         private val btn1: View, private val btn2: View, private val btn3: View, private val btn4: View) {

        init {
            lets.also { it.translationX = -800F }
            play.also { it.translationX = 800F }
            darts.also { it.translationX = -800F }
            btn1.also(buttonStart(0))
            btn2.also(buttonStart(1))
            btn3.also(buttonStart(2))
            btn4.also(buttonStart(3))
        }

        fun init() {
            buttonAnimation(btn1, 0)
            buttonAnimation(btn2, 1)
            buttonAnimation(btn3, 2)
            buttonAnimation(btn4, 2)

            lets.animate().translationX(0F).setDuration(250).setInterpolator(AccelerateDecelerateInterpolator()).start()
            play.animate().translationX(0F).setStartDelay(250).setDuration(250).setInterpolator(AccelerateDecelerateInterpolator()).start()
            darts.animate().translationX(0F).setStartDelay(500).setDuration(250).setInterpolator(AccelerateDecelerateInterpolator()).start()
        }

        private fun buttonAnimation(btn: View, index: Int) {
            btn.animate().alpha(1F).translationY(0F).setStartDelay(index * 26L).setInterpolator(OvershootInterpolator()).start()
        }

        private fun buttonStart(index: Int): (View) -> Unit = {
            it.translationY = 100F * index
            it.alpha = 0F
        }
    }
}
