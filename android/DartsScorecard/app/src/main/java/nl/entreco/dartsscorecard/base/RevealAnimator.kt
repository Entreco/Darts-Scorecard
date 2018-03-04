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

    fun setupEnterAnimation(inflater: TransitionInflater?, window: Window, root: View) {
        if(inflater == null) return
        val transition = inflater.inflateTransition(R.transition.change_bound_with_arc)
        transition.duration = 100
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                animateRevealShow(root)
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
                root.visibility = View.INVISIBLE
            }
        })
    }

    fun setupExitAnimation(inflater: TransitionInflater?, window: Window, root: View) {
        if(inflater == null) return
        val transition = inflater.inflateTransition(R.transition.change_bound_with_arc)
        transition.duration = 100
        window.sharedElementExitTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                animateRevealHide(root)
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
                root.visibility = View.VISIBLE
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

    private fun animateRevealHide(root: View) {
        val cx = (target.left + target.right) / 2
        val cy = (target.top + target.bottom) / 2
        unrevealActivity(root, cx, cy)
    }

    private fun unrevealActivity(root: View, x: Int, y: Int) {
        val radius = max(root.width * 1.0, root.height * 1.1).toFloat()
        val circularReveal = ViewAnimationUtils.createCircularReveal(root, x, y, radius, 0F)
        circularReveal.duration = 200
        circularReveal.interpolator = AccelerateInterpolator()
        root.visibility = View.INVISIBLE
        circularReveal.start()
    }
}
