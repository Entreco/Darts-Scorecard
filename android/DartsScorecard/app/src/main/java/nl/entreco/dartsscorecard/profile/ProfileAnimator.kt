package nl.entreco.dartsscorecard.profile

import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by entreco on 23/02/2018.
 */
class ProfileAnimator(binding: ActivityProfileBinding) {

    private var isHideToolbarView = false
    private val appBar = binding.includeAppbar?.profileAppbar!!
    private val collapsedHeader = binding.includeAppbar?.includeHeaderViewTop?.toolbarHeaderView!!
    private val expandedHeader = binding.includeAppbar?.includeHeaderView?.toolbarHeaderView!!
    private val expandedImage = binding.includeAppbar?.includeHeaderView?.image!!
    private val expandedName: TextView = binding.includeAppbar?.includeHeaderView?.name!!
    private val collapsedName: TextView = binding.includeAppbar?.includeHeaderViewTop?.name!!
    private val collapsedImage = binding.includeAppbar?.includeHeaderViewTop?.image!!

    init {
        val start = expandedName.textSize
        val end = collapsedName.textSize

        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()
            val width = appBarLayout.width / 2

            val height by lazy { collapsedImage.height }
            val orig: Double by lazy { height.toFloat() / expandedImage.height.toDouble() }

            val scale = max(1.0 - percentage, orig).toFloat()
            expandedImage.animate()
                    .translationX(width * percentage)
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(0).start()

            val textSize = (start - end) * (1 - percentage) + end
            Log.w("HAHA", "w:$width perc: $percentage = ${-(width-20)*percentage}")
            expandedName.animate()
                    .translationX( -width*percentage)
                    .translationY(-expandedName.top * percentage)
                    .withEndAction { expandedName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize) }
                    .setDuration(0).start()

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
    }
}
