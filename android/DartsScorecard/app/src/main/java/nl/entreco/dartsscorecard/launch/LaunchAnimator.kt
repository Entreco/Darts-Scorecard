package nl.entreco.dartsscorecard.launch

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import nl.entreco.dartsscorecard.databinding.ActivityLaunchBinding

/**
 * Created by Entreco on 19/12/2017.
 */
class LaunchAnimator(binding: ActivityLaunchBinding) {

    private val lets = binding.includeLaunchHeader?.lets!!.also { it.translationX = -800F }
    private val play = binding.includeLaunchHeader?.play!!.also { it.translationX = 800F }
    private val darts = binding.includeLaunchHeader?.dart!!.also { it.translationX = -800F }

    private val btn1 = binding.launchResume.also(buttonStart(0))
    private val btn2 = binding.launchPlay.also(buttonStart(1))
    private val btn3 = binding.launchPlayers.also(buttonStart(2))
    private val btn4 = binding.launchStats.also(buttonStart(3))

    init {
        buttonAnimation(btn1, 0)
        buttonAnimation(btn2, 1)
        buttonAnimation(btn3, 2)
        buttonAnimation(btn4, 3)

        lets.animate().translationX(0F).setDuration(250).setInterpolator(AccelerateDecelerateInterpolator()).start()
        play.animate().translationX(0F).setStartDelay(250).setDuration(250).setInterpolator(AccelerateDecelerateInterpolator()).start()
        darts.animate().translationX(0F).setStartDelay(500).setDuration(250).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    private fun buttonAnimation(btn: View, index: Int) {
        btn.animate().alpha(1F).translationY(0F).setStartDelay(index * 26L).setInterpolator(OvershootInterpolator()).start()
    }

    private fun buttonStart(index: Int): (Button) -> Unit = {
        it.translationY = 100F * index
        it.alpha = 0F
    }
}
