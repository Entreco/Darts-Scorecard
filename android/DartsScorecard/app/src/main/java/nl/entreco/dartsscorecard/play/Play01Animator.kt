package nl.entreco.dartsscorecard.play

import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.view.ViewPropertyAnimator
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by Entreco on 02/12/2017.
 */
class Play01Animator(binding: ActivityPlay01Binding) {

    private val fab = binding.includeInput?.fab!!
    private val inputSheet = binding.includeInput?.inputSheet!!
    private val inputResume = binding.includeInput?.inputResume!!
    private val mainSheet = binding.includeMain?.mainSheet!!
    private val behaviour = BottomSheetBehavior.from(inputSheet)
    private val stat1 = binding.includeMain?.stat1!!
    private val stat2 = binding.includeMain?.stat2!!
    private val stat3 = binding.includeMain?.stat3!!
    private val stat4 = binding.includeMain?.stat4!!
    private val stat5 = binding.includeMain?.stat5!!
    private val stat6 = binding.includeMain?.stat6!!
    private val stat7 = binding.includeMain?.stat7!!
    private val player1 = binding.includeMain?.player1!!
    private val player2 = binding.includeMain?.player2!!
    private val version = binding.includeMain?.version!!

    init {
        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                // Scale Fab Out Bottom/Top
                fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()

                // Fade In MainSheet
                mainSheet.animate().alpha(1 - sqrt(slideOffset)).setDuration(0).start()
                mainSheet.animate().translationY((slideOffset) * -100).setDuration(0).start()

                // Fly In Players
                player1.animate().translationX(slideOffset * -player1.width / 3).setDuration(0).start()
                player2.animate().translationX(slideOffset * player2.width / 3).setDuration(0).start()

                // Fly In stats
                animateState(stat1.animate(), 1, slideOffset)
                animateState(stat2.animate(), 2, slideOffset)
                animateState(stat3.animate(), 3, slideOffset)
                animateState(stat4.animate(), 4, slideOffset)
                animateState(stat5.animate(), 5, slideOffset)
                animateState(stat6.animate(), 6, slideOffset)
                animateState(stat7.animate(), 7, slideOffset)

                // Also, Fly In Version
                animateState(version.animate(), 8, slideOffset)

                // Show Resume
                inputResume.animate().alpha(1 - slideOffset).translationX(slideOffset * -inputResume.width).setDuration(0).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        inputResume.setOnClickListener { expand() }
                    }
                    else -> inputResume.setOnClickListener(null)
                }
            }
        })

        expand()
    }

    private fun expand() {
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun animateState(anim: ViewPropertyAnimator, index: Int, slideOffset: Float) {
        anim.translationY(-index * 50 * slideOffset * index).scaleX(max(0f, (1 - slideOffset * index))).alpha(1 - slideOffset).setDuration(0).start()
    }
}