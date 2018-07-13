package nl.entreco.dartsscorecard.profile.view

import android.support.design.widget.AppBarLayout
import android.transition.TransitionInflater
import android.view.View
import android.view.Window
import android.view.animation.AlphaAnimation
import nl.entreco.dartsscorecard.base.RevealAnimator
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import kotlin.math.abs


/**
 * Created by entreco on 23/02/2018.
 */
class ProfileAnimator(binding: ActivityProfileBinding, inflater: TransitionInflater, window: Window) {

    private val appBarLayout = binding.mainAppbar
    private val title = binding.mainTextviewTitle
    private val titleContainer = binding.mainLinearlayoutTitle
    private val animator = ProfileAnimatorHandler(title, titleContainer)
    private val revealAnimator = RevealAnimator(appBarLayout)

    init {
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            animator.onOffsetChanged(appBarLayout, verticalOffset)
        }

        revealAnimator.setupEnterAnimation(inflater, window, true)
        animator.startAlphaAnimation(title, 0, View.INVISIBLE)
    }

    internal class ProfileAnimatorHandler(private val title: View, private val titleContainer: View) {

        companion object {
            private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
            private const val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
            private const val ALPHA_ANIMATIONS_DURATION: Long = 200
        }

        private var mIsTheTitleVisible = false
        private var mIsTheTitleContainerVisible = true

        fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
            val maxScroll = appBarLayout.totalScrollRange
            val percentage: Float = abs(offset) / maxScroll.toFloat()

            handleAlphaOnTitle(percentage)
            handleToolbarTitleVisibility(percentage)
        }

        private fun handleToolbarTitleVisibility(percentage: Float) {
            if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

                if (!mIsTheTitleVisible) {
                    startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                    mIsTheTitleVisible = true
                }

            } else {

                if (mIsTheTitleVisible) {
                    startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                    mIsTheTitleVisible = false
                }
            }
        }

        private fun handleAlphaOnTitle(percentage: Float) {
            if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
                if (mIsTheTitleContainerVisible) {
                    startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                    mIsTheTitleContainerVisible = false
                }

            } else {

                if (!mIsTheTitleContainerVisible) {
                    startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                    mIsTheTitleContainerVisible = true
                }
            }
        }

        fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
            val alphaAnimation = if (visibility == View.VISIBLE)
                AlphaAnimation(0f, 1f)
            else
                AlphaAnimation(1f, 0f)

            alphaAnimation.duration = duration
            alphaAnimation.fillAfter = true
            v.startAnimation(alphaAnimation)
        }
    }
}
