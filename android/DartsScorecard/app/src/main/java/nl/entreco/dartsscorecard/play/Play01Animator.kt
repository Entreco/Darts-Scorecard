package nl.entreco.dartsscorecard.play

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.view.*
import kotlinx.android.synthetic.main.activity_play_01.view.*
import kotlinx.android.synthetic.main.play_01_score.view.*
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.databinding.WidgetListStatsBinding
import nl.entreco.dartsscorecard.play.stats.MatchStatAnimator
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by Entreco on 02/12/2017.
 */
class Play01Animator(binding: ActivityPlay01Binding) {

    private val pager = binding.includeMain?.statPager!!
    private val teamSheet = binding.includeScore?.teamContainer!!
    private val inputSheet = binding.includeInput?.inputSheet!!
    private val behaviour = BottomSheetBehavior.from(inputSheet)
    private val animator = Play01AnimatorHandler(binding.includeScore?.scoreSheet!!, binding.includeInput?.fab!!, binding.includeMain?.mainSheet!!, binding.includeMain.version, binding.includeInput.inputResume)

    init {

        calculateHeightForScoreView(binding)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val view = pager.getChildAt(position)
                val statBinding = DataBindingUtil.getBinding<WidgetListStatsBinding>(view)
                animator.onPageSelected(statBinding)
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

    companion object {
        @JvmStatic
        @BindingAdapter("loading")
        fun showLoading(view: ViewGroup, loading: Boolean) {
            if (loading) {
                val loadingView = LayoutInflater.from(view.context).inflate(R.layout.play_01_loading, view, false)
                view.addView(loadingView)
                loadingView.animate().alpha(1F).start()
            } else {
                val count = view.childCount - 1
                val loadingView = view.getChildAt(count)
                loadingView.animate().alpha(0F)
                        .withEndAction { view.removeView(loadingView) }
                        .start()
            }
        }

        @JvmStatic
        @BindingAdapter("snack")
        fun showSnack(view: View, @StringRes msg: Int) {
            if (msg > 0) {
                Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
            }
        }

        @JvmStatic
        @BindingAdapter("finished", "animator")
        fun showGameFinished(view: CoordinatorLayout, finished: Boolean, animator: Play01Animator?) {
            if (finished) {
                animator?.collapse()
            }
        }
    }

    internal class Play01AnimatorHandler(private val scoreSheet: View, private val fab: View, private val mainSheet: View, private val version: View, private val inputResume: View) {

        var statAnimator: MatchStatAnimator? = null

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

        fun onPageSelected(binding: WidgetListStatsBinding?) {
            if (binding != null) {
                statAnimator = MatchStatAnimator(binding)
            }
        }
    }
}
