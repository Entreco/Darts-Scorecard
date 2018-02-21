package nl.entreco.dartsscorecard.profile

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import nl.entreco.dartsscorecard.R


/**
 * Created by entreco on 21/02/2018.
 */
class ProfileHeaderBehaviour(private val context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<ProfileHeaderView>(context, attrs) {

    private var startMarginLeft = 0
    private var endMarginLeft = 0
    private var startMarginBottom = 0
    private var marginRight = 0
    private var titleEndSize = 0
    private var titleStartSize = 0
    private var isHide: Boolean = false

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: ProfileHeaderView?, dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    private fun getToolbarHeight(context: Context): Int {
        var result = 0
        val typedValue = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            result = TypedValue.complexToDimensionPixelSize(typedValue.data, context.resources.displayMetrics)
        }
        return result
    }

    private fun getTranslationOffset(expandedOffset: Int, collapsedOffset: Int, ratio: Float): Float {
        return expandedOffset + ratio * (collapsedOffset - expandedOffset)
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: ProfileHeaderView?, dependency: View?): Boolean {
        shouldInitProperties()

        if (child == null || context == null) return false

        val maxScroll = (dependency as AppBarLayout).totalScrollRange
        val percentage = Math.abs(dependency.getY()) / maxScroll.toFloat()
        var childPosition = (dependency.getHeight() + dependency.getY() - child.height - (getToolbarHeight(context) - child.height) * percentage / 2)

        childPosition -= startMarginBottom * (1f - percentage)

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        if (Math.abs(dependency.getY()) >= maxScroll / 2) {
            val layoutPercentage = (Math.abs(dependency.getY()) - maxScroll / 2) / Math.abs(maxScroll / 2)
            lp.leftMargin = ((layoutPercentage * endMarginLeft) + startMarginLeft).toInt()
            child.setTextSize(getTranslationOffset(titleStartSize, titleEndSize, layoutPercentage))
        } else {
            lp.leftMargin = startMarginLeft
        }
        lp.rightMargin = marginRight
        child.layoutParams = lp
        child.y = childPosition

        if (isHide && percentage < 1) {
            child.visibility = View.VISIBLE
            isHide = false
        } else if (!isHide && percentage == 1f) {
            child.visibility = View.GONE
            isHide = true
        }
        return true
    }

    private fun shouldInitProperties() {
        if (context == null) return

        if (startMarginLeft == 0) {
            startMarginLeft = context.resources.getDimensionPixelOffset(R.dimen.header_view_start_margin_left)
        }

        if (endMarginLeft == 0) {
            endMarginLeft = context.resources.getDimensionPixelOffset(R.dimen.header_view_end_margin_left)
        }

        if (startMarginBottom == 0) {
            startMarginBottom = context.resources.getDimensionPixelOffset(R.dimen.header_view_start_margin_bottom)
        }

        if (marginRight == 0) {
            marginRight = context.resources.getDimensionPixelOffset(R.dimen.header_view_end_margin_right)
        }

        if (titleEndSize == 0) {
            titleEndSize = context.resources.getDimensionPixelSize(R.dimen.header_view_end_text_size)
        }

        if (titleStartSize == 0) {
            titleStartSize = context.resources.getDimensionPixelSize(R.dimen.header_view_start_text_size)
        }
    }
}
