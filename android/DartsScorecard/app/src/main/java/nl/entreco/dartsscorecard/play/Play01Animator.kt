package nl.entreco.dartsscorecard.play

import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_play_01.view.*
import kotlinx.android.synthetic.main.play_01_score.view.*
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by Entreco on 02/12/2017.
 */
class Play01Animator(binding: ActivityPlay01Binding) {

    private val fab = binding.includeInput?.fab!!
    private val scoreSheet = binding.includeScore?.scoreSheet!!
    private val teamSheet = binding.includeScore?.teamContainer!!
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
    private val name1 = binding.includeMain?.name1!!
    private val name2 = binding.includeMain?.name2!!
    private val score = binding.includeMain?.score!!
    private val player1 = binding.includeMain?.player1!!
    private val player2 = binding.includeMain?.player2!!
    private val version = binding.includeMain?.version!!

    init {

        calculateHeightForScoreView(binding)

        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                // Slide Out ScoreViewModel
                scoreSheet.animate().alpha(slideOffset).translationY(-scoreSheet.height * (1 - slideOffset)).setDuration(0).start()

                // Scale Fab Out Bottom/Top
                fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()

                // Fade In MainSheet
                mainSheet.animate().alpha(1 - sqrt(slideOffset)).setDuration(0).start()
                mainSheet.animate().translationY((slideOffset) * -100).setDuration(0).start()

                // Fly In Players
                player1.animate().translationX(slideOffset * -player1.width / 3).setDuration(0).start()
                player2.animate().translationX(slideOffset * player2.width / 3).setDuration(0).start()
                name1.animate().translationX(slideOffset * -name1.width).setDuration(0).start()
                name2.animate().translationX(slideOffset * name2.width).setDuration(0).start()
                score.animate().alpha(1 - slideOffset).scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start()

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
        Handler().postDelayed({ collapse() }, 100)
    }

    private fun expand() {
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun collapse() {
        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun animateState(anim: ViewPropertyAnimator, index: Int, slideOffset: Float) {
        anim.translationY(-index * 50 * slideOffset * index).scaleX(max(0f, (1 - slideOffset * index))).alpha(1 - slideOffset).setDuration(0).start()
    }

    private fun calculateHeightForScoreView(binding: ActivityPlay01Binding) {
        binding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.root.viewTreeObserver.removeOnPreDrawListener(this)

                val input = inputSheet.height
                val header = binding.root.includeScore.header.height
                val footer = binding.root.includeScore.footer.height
                val toolbar = binding.root.includeToolbar.height
                teamSheet.maxHeight = binding.root.height - toolbar - header - footer - input - 100
                teamSheet.requestLayout()
                return true
            }
        })
    }
}