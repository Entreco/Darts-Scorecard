package nl.entreco.dartsscorecard.beta

import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import kotlinx.android.synthetic.main.include_beta_detail.view.*
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by entreco on 04/02/2018.
 */
class BetaAnimator(binding: ActivityBetaBinding) {

    private val behaviour: BottomSheetBehavior<View> = BottomSheetBehavior.from(binding.sheet)!!
    private val appBar = binding.includeToolbar?.betaAppbar!!
    private val animator = BetaAnimatorHandler(appBar, binding.includeToolbar?.toolbar!!, binding.sheet, binding.sheet.voteFab)
    internal var toggler: Toggler? = null

    interface Toggler {
        fun onFeatureSelected(feature: BetaModel)
    }

    init {

        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            animator.onOffsetChanged(appBarLayout, verticalOffset)
        }

        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                animator.onSlide(slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                animator.onStateChanged(newState)
            }
        })

        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun vote(model: BetaModel) {
        if (model.title.get() != null) {
            appBar.setExpanded(false, true)
            appBar.isEnabled = false
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            toggler?.onFeatureSelected(model)
        }
    }

    fun onBackPressed(): Boolean? {
        if (behaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
            behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            return false
        }
        return null
    }

    internal class BetaAnimatorHandler(private val appBar: AppBarLayout, private val toolbar: View, private val sheet: View, private val fab: View) {
        fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            val perc = abs(verticalOffset / appBarLayout.totalScrollRange.toFloat())
            toolbar.animate().alpha(perc).setDuration(0).start()
        }

        fun onSlide(slideOffset: Float) {
            val perc = max(0.85F, slideOffset)
            val rot = 5 - 5 * slideOffset
            sheet.animate().scaleX(perc).scaleY(perc).rotationX(-rot).setDuration(0).start()

            fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()
        }

        fun onStateChanged(newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    appBar.isEnabled = true
                }
            }
        }
    }
}
