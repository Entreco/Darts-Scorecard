package nl.entreco.dartsscorecard.profile.view

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import de.hdodenhof.circleimageview.CircleImageView
import nl.entreco.dartsscorecard.R

class ProfileHeaderBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<CircleImageView>(context, attrs) {

    private var customFinalYPosition: Float = 0F
    private var customStartXPosition: Float = 0F
    private var customStartToolbarPosition: Float = 0F
    private var customStartHeight: Float = 0F
    private var customFinalHeight: Float = 0F
    private var avatarMaxSize: Float = 0F
    private var finalLeftAvatarPadding: Float = 0F

    private var startXPosition: Int = 0
    private var startYPosition: Int = 0
    private var finalYPosition: Int = 0
    private var startHeight: Int = 0
    private var finalXPosition: Int = 0

    private var startToolbarPosition: Float = 0F
    private var changeBehaviorPoint: Float = 0F

    private var dpo: Float

    init {
        context?.obtainStyledAttributes(attrs, R.styleable.ProfileHeaderBehavior)?.apply {

            customFinalYPosition = getDimension(R.styleable.ProfileHeaderBehavior_finalYPosition, 0F)
            customStartXPosition = getDimension(R.styleable.ProfileHeaderBehavior_startXPosition, 0F)
            customStartToolbarPosition = getDimension(R.styleable.ProfileHeaderBehavior_startToolbarPosition, 0F)
            customStartHeight = getDimension(R.styleable.ProfileHeaderBehavior_startHeight, 0F)
            customFinalHeight = getDimension(R.styleable.ProfileHeaderBehavior_finalHeight, 0F)

            recycle()
        }

        avatarMaxSize = context?.resources?.getDimension(R.dimen.header_profile_pic_size)!!
        dpo = context.resources.getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material).toFloat()
        finalLeftAvatarPadding = context.resources.getDimension(R.dimen.large)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: CircleImageView?, dependency: View?): Boolean {
        return dependency is Toolbar || dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: CircleImageView, dependency: View): Boolean {

        if (dependency is AppBarLayout) return false

        maybeInitProperties(child, dependency)

        val maxScrollDistance = startToolbarPosition.toInt()
        val expandedPercentageFactor = dependency.y / maxScrollDistance

        if (expandedPercentageFactor < changeBehaviorPoint) {
            val heightFactor = (changeBehaviorPoint - expandedPercentageFactor) / changeBehaviorPoint
            val distanceXToSubtract = ((startXPosition - finalXPosition) * heightFactor) + (child.height / 2)
            val distanceYToSubtract = ((startYPosition - finalYPosition) * (1F - expandedPercentageFactor)) + (child.height / 2)

            child.x = startXPosition - distanceXToSubtract
            child.y = startYPosition - distanceYToSubtract

            val heightToSubtract = ((startHeight - customFinalHeight) * heightFactor)
            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = startHeight - heightToSubtract.toInt()
            lp.height = startHeight - heightToSubtract.toInt()
            child.layoutParams = lp

        } else {

            val distanceYToSubtract = ((startYPosition - finalYPosition) * (1F - expandedPercentageFactor)) + (startHeight / 2)
            child.x = (startXPosition - child.width / 2).toFloat()
            child.y = startYPosition - distanceYToSubtract


            val lp = child.layoutParams as CoordinatorLayout.LayoutParams
            lp.width = startHeight
            lp.height = startHeight
            child.layoutParams = lp

        }
        return true
    }

    private fun maybeInitProperties(child: CircleImageView, dependency: View) {
        if (startYPosition == 0)
            startYPosition = dependency.y.toInt()

        if (finalYPosition == 0)
            finalYPosition = dependency.height / 2

        if (startHeight == 0)
            startHeight = child.height

        if (startXPosition == 0)
            startXPosition = (child.x + (child.width / 2)).toInt()

        if (finalXPosition == 0)
            finalXPosition = dpo.toInt() + (customFinalHeight / 2).toInt()

        if (startToolbarPosition == 0F)
            startToolbarPosition = dependency.y

        if (changeBehaviorPoint == 0F) {
            changeBehaviorPoint = (child.height - customFinalHeight) / (2f * (startYPosition - finalYPosition))
        }
    }

}