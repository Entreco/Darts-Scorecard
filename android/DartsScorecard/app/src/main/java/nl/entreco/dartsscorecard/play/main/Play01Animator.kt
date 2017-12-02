package nl.entreco.dartsscorecard.play.main

import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding

/**
 * Created by Entreco on 02/12/2017.
 */
class Play01Animator(binding: ActivityPlay01Binding) {

    private val fab = binding.includeInput?.fab!!
    private val inputSheet = binding.includeInput?.inputSheet!!
    private val mainSheet = binding.includeMain?.mainSheet!!
    private val behaviour = BottomSheetBehavior.from(inputSheet)

    init {
        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                // Slide Out Bottom/Top
                fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()

                // Fade Out
                inputSheet.animate().alpha(slideOffset).setDuration(0).start()

                // Fade In MainSheet
                mainSheet.animate().alpha(1 - Math.sqrt(slideOffset.toDouble()).toFloat()).setDuration(0).start()
                mainSheet.animate().translationY((slideOffset) * -100).setDuration(0).start()

                // Fly In stats
                animateState(binding.includeMain?.stat1!!, 100, slideOffset)
                animateState(binding.includeMain?.stat2!!, 200, slideOffset)
                animateState(binding.includeMain?.stat3!!, 300, slideOffset)
                animateState(binding.includeMain?.stat4!!, 400, slideOffset)
                animateState(binding.includeMain?.stat5!!, 500, slideOffset)
                animateState(binding.includeMain?.stat6!!, 600, slideOffset)
                animateState(binding.includeMain?.stat7!!, 700, slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun animateState(view: LinearLayout, index: Int, slideOffset: Float) {
        view.animate().translationY(-index * slideOffset).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(0).start()
    }
}