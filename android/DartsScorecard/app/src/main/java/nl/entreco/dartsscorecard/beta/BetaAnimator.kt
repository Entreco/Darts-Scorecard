package nl.entreco.dartsscorecard.beta

import android.databinding.ObservableField
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by entreco on 04/02/2018.
 */
class BetaAnimator(binding: ActivityBetaBinding) {

    val selectedFeature = ObservableField<BetaModel>()

    private val sheet = binding.sheet
    private val behaviour: BottomSheetBehavior<View> = BottomSheetBehavior.from(sheet)!!
    private val appBar = binding.includeToolbar?.betaAppbar!!
    private val toolbar = binding.includeToolbar?.toolbar!!

    init {

        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val perc = abs(verticalOffset / appBarLayout.totalScrollRange.toFloat())
            toolbar.animate().alpha(perc).setDuration(0).start()
        }

        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val perc = max(0.85F, slideOffset)
                val rot = 5 - 5 * slideOffset
                sheet.animate().scaleX(perc).scaleY(perc).rotationX(-rot).setDuration(0).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })

        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun vote(feature: BetaModel) {
        appBar.setExpanded(false, true)
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
        selectedFeature.set(feature)
    }

    fun onBackPressed(): Boolean? {
        if (behaviour.state == BottomSheetBehavior.STATE_EXPANDED) {
            behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            return false
        }
        return null
    }
}