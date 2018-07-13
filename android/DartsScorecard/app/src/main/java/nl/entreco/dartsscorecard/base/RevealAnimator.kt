package nl.entreco.dartsscorecard.base

import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.Window
import android.view.animation.AccelerateInterpolator
import nl.entreco.dartsscorecard.R
import kotlin.math.max

/**
 * Created by entreco on 02/03/2018.
 */
class RevealAnimator(private val target: View) {

    fun setupEnterAnimation(inflater: TransitionInflater?, window: Window, reverse: Boolean = true) {
        if (inflater == null) return
        val boundsTransition = inflater.inflateTransition(R.transition.change_bound_with_arc)
        boundsTransition.duration = 100
        window.sharedElementEnterTransition = boundsTransition
        boundsTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                if (!reverse) {
                    animateRevealShow(target.rootView)
                }
                boundsTransition.removeListener(this)
            }

            override fun onTransitionResume(transition: Transition?) {}

            override fun onTransitionPause(transition: Transition?) {}

            override fun onTransitionCancel(transition: Transition?) {}

            override fun onTransitionStart(transition: Transition?) {
                if (!reverse) {
                    target.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun animateRevealShow(root: View) {
        val cx = (target.left + target.right) / 2
        val cy = (target.top + target.bottom) / 2
        revealActivity(root, cx, cy)
    }

    private fun revealActivity(root: View, x: Int, y: Int) {
        val radius = max(root.width * 1.0, root.height * 1.1).toFloat()
        val circularReveal = ViewAnimationUtils.createCircularReveal(root, x, y, 0F, radius)
        circularReveal.duration = 200
        circularReveal.interpolator = AccelerateInterpolator()
        root.visibility = View.VISIBLE
        circularReveal.start()
    }
}
