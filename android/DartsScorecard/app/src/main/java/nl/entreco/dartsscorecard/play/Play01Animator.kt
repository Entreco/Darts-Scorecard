package nl.entreco.dartsscorecard.play

import android.databinding.DataBindingUtil
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_play_01.view.*
import kotlinx.android.synthetic.main.play_01_score.view.*
import nl.entreco.dartsscorecard.base.widget.MaxHeightRecyclerView
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding
import nl.entreco.dartsscorecard.play.stats.MatchStatSlideAnimator
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by Entreco on 02/12/2017.
 */
class Play01Animator(binding: ActivityPlay01Binding) {

    private val pager = binding.includeMain?.statPager!!
    private val inputSheet = binding.includeInput?.inputSheet!!
    private val behaviour = BottomSheetBehavior.from(inputSheet)
    private val animator = Play01AnimatorHandler(binding.root, binding.includeScore?.scoreSheet!!, binding.includeInput?.fab!!, binding.includeMain?.mainSheet!!, binding.includeMain.version, binding.includeInput.inputResume, pager, binding.includeScore.teamContainer, inputSheet, binding.root.includeScore.header, binding.root.includeScore.footer, binding.root.includeToolbar)

    init {
        calculateHeightForScoreView(binding)
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                animator.setPage(position)
            }
        })

        behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                animator.onSlide(slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })

        expand()
    }

    internal fun expand() {
        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    internal fun collapse() {
        behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun calculateHeightForScoreView(binding: ActivityPlay01Binding) {
        binding.root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                animator.onPreDraw()
                return true
            }
        })
    }

    internal class Play01AnimatorHandler(private val root: View, private val scoreSheet: View, private val fab: View, private val mainSheet: View, private val version: View, private val inputResume: View, private val pager: ViewPager, private val teamSheet: MaxHeightRecyclerView, private val inputSheet: View, private val scoreHeader: View, private val scoreFooter: View, private val toolbar: View) {

        private var statAnimator: MatchStatSlideAnimator? = null

        fun onSlide(slideOffset: Float) {
            // Slide Out ScoreViewModel
            scoreSheet.animate().alpha(slideOffset).translationY(-scoreSheet.height * (1 - slideOffset)).setDuration(0).start()

            statAnimator?.onSlide(slideOffset)

            // Scale Fab Out Bottom/Top
            fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()

            // Fade In MainSheet
            mainSheet.animate().alpha(1 - sqrt(slideOffset)).setDuration(0).start()
            mainSheet.animate().translationY((slideOffset) * -100).setDuration(0).start()

            // Also, Fly In Version
            animateState(version.animate(), 8, slideOffset)

            // Show Resume
            inputResume.animate().alpha(1 - slideOffset).translationX(slideOffset * -inputResume.width).setDuration(0).start()
        }

        private fun animateState(anim: ViewPropertyAnimator, index: Int, slideOffset: Float) {
            anim.translationY(-index * 50 * slideOffset * index).scaleX(max(0f, (1 - slideOffset * index))).alpha(1 - slideOffset).setDuration(0).start()
        }

        fun setPage(position: Int) {
            val view = pager.getChildAt(position)
            val binding = DataBindingUtil.getBinding<WidgetListStatsBinding>(view)
            if (binding != null) {
                statAnimator = MatchStatSlideAnimator(binding, pager.getChildAt(position - 1), pager.getChildAt(position + 1))
            }
        }

        fun onPreDraw() {
            val input = inputSheet.height
            val header = scoreHeader.height
            val footer = scoreFooter.height
            val toolbar = toolbar.height
            teamSheet.maxHeight = root.height - toolbar - header - footer - input - 100
            teamSheet.requestLayout()
        }
    }
}
