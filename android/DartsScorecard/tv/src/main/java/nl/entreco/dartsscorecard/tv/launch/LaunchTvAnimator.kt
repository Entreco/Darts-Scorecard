package nl.entreco.dartsscorecard.tv.launch

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import nl.entreco.dartsscorecard.tv.databinding.ActivityTvLaunchBinding

class LaunchTvAnimator(binding: ActivityTvLaunchBinding) {

    private val animator = LaunchAnimatorHandler(
            binding.includeLaunchHeader.lets,
            binding.includeLaunchHeader.play,
            binding.includeLaunchHeader.dart)

    init {
        animator.init()
    }

    internal class LaunchAnimatorHandler(private val lets: View, private val play: View,
                                         private val darts: View) {

        private val horizontalTranslation: Float = 800F
        private val duration: Long = 250
        private val durationTwice: Long = 500

        init {
            lets.also { it.translationX = -horizontalTranslation }
            play.also { it.translationX = horizontalTranslation }
            darts.also { it.translationX = -horizontalTranslation }
        }


        fun init() {
            lets.animate().translationX(0F).setDuration(duration).setInterpolator(
                    AccelerateDecelerateInterpolator()).start()
            play.animate().translationX(0F).setStartDelay(duration).setDuration(duration)
                    .setInterpolator(
                            AccelerateDecelerateInterpolator()).start()
            darts.animate().translationX(0F).setStartDelay(durationTwice).setDuration(duration)
                    .setInterpolator(
                            AccelerateDecelerateInterpolator()).start()
        }
    }
}