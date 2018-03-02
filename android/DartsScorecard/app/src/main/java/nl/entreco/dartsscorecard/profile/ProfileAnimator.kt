package nl.entreco.dartsscorecard.profile

import android.transition.TransitionInflater
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.TextView
import nl.entreco.dartsscorecard.base.RevealAnimator
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by entreco on 23/02/2018.
 */
class ProfileAnimator(binding: ActivityProfileBinding, inflater: TransitionInflater, window: Window) {

    private var isHideToolbarView = false
    private val appBar = binding.includeAppbar?.profileAppbar!!
    private val collapsedHeader = binding.includeAppbar?.includeHeaderViewTop?.toolbarHeaderView!!
    private val expandedHeader = binding.includeAppbar?.includeHeaderView?.toolbarHeaderView!!
    private val expandedImage = binding.includeAppbar?.includeHeaderView?.image!!
    private val expandedName: TextView = binding.includeAppbar?.includeHeaderView?.profileHeaderName!!
    private val expandedDouble: TextView = binding.includeAppbar?.includeHeaderView?.favDouble!!
    private val collapsedName: TextView = binding.includeAppbar?.includeHeaderViewTop?.name!!
    private val collapsedImage = binding.includeAppbar?.includeHeaderViewTop?.image!!
    private val fab = binding.fab

    private val revealAnimator = RevealAnimator(expandedImage)

    init {
        val start = expandedName.textSize
        val end = collapsedName.textSize

        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()
            val width = appBarLayout.width / 2
            val orig: Double by lazy { collapsedImage.height.toFloat() / expandedImage.height.toDouble() }

            animateImage(percentage, orig, width)
            animateTitle(start, end, width, percentage)
            animateFavDouble(percentage)
            animateFab(percentage)

            if (percentage == 1f && isHideToolbarView) {
                collapsedHeader.visibility = View.VISIBLE
                expandedHeader.visibility = View.GONE
                isHideToolbarView = !isHideToolbarView

            } else if (percentage < 1f && !isHideToolbarView) {
                collapsedHeader.visibility = View.GONE
                expandedHeader.visibility = View.VISIBLE
                isHideToolbarView = !isHideToolbarView
            }
        }

        revealAnimator.setupEnterAnimation(inflater, window, binding.root)
    }

    private fun animateFab(percentage: Float) {
        fab.animate().scaleX(1 - percentage).scaleY(1 - percentage).alpha(1 - percentage).setDuration(0).start()
    }

    private fun animateImage(percentage: Float, orig: Double, width: Int) {
        val scale = max(1.0 - percentage, orig).toFloat()
        expandedImage.animate()
                .translationX((width - 20) * percentage)
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(0).start()
    }

    private fun animateTitle(start: Float, end: Float, width: Int, percentage: Float) {
        val textSize = (start - end) * (1 - percentage) + end
        expandedName.animate()
                .translationX(-(width - expandedName.width / 2) * percentage)
                .translationY(-expandedName.y * percentage / 2)
                .withEndAction { expandedName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize) }
                .setDuration(0).start()
    }

    private fun animateFavDouble(percentage: Float) {
        expandedDouble.animate().alpha(1 - percentage).setDuration(0).start()
    }
}
