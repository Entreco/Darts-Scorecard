package nl.entreco.dartsscorecard.play

import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_play_01.view.*
import kotlinx.android.synthetic.main.play_01_score.view.*
import nl.entreco.dartsscorecard.base.widget.MaxHeightRecyclerView
import nl.entreco.dartsscorecard.databinding.ActivityPlay01Binding
import nl.entreco.dartsscorecard.play.live.LiveStatSlideAnimator
import kotlin.math.max
import kotlin.math.sqrt

/**
 * Created by Entreco on 02/12/2017.
 */
class Play01Animator(binding: ActivityPlay01Binding) {

    private val pager = binding.includeMain.statPager
    private val inputSheet = binding.includeInput.inputSheet
    private val behaviour = BottomSheetBehavior.from(inputSheet)
    private val animator = Play01AnimatorHandler(binding.root, binding.includeScore.scoreSheet, binding.includeInput.fab, binding.includeMain.mainSheet, binding.includeMain.version,
            binding.includeInput.inputResume, pager, binding.includeScore.teamContainer, inputSheet, binding.root.includeScore.header, binding.root.includeScore.footer, binding.root.includeToolbar)

    init {
        animator.calculateHeightForScoreView()
        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                animator.storePositionForAnimator(position)
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

    internal class Play01AnimatorHandler(private val root: View, private val scoreSheet: View, private val fab: View, private val mainSheet: View, private val version: View,
                                         private val inputResume: View, private val pager: ViewPager, private val teamSheet: MaxHeightRecyclerView, private val inputSheet: View,
                                         private val scoreHeader: View, private val scoreFooter: View, private val toolbar: View) {

        private var animatorPosition: Int = 0
        internal var animator: LiveStatSlideAnimator? = null
        private val lock = Object()

        fun onSlide(slideOffset: Float) {
            // Slide Out ScoreViewModel
            scoreSheet.animate().alpha(slideOffset).translationY(-scoreSheet.height * (1 - slideOffset)).setDuration(0).start()


            // Scale Fab Out Bottom/Top
            fab.animate().scaleY(slideOffset).scaleX(slideOffset).setDuration(0).start()

            // Fade In MainSheet
            mainSheet.animate().alpha(1 - sqrt(slideOffset)).setDuration(0).start()
            mainSheet.animate().translationY((slideOffset) * -100).setDuration(0).start()

            // Also, Fly In Version
            animateState(version.animate(), 8, slideOffset)

            // Show Resume
            inputResume.animate().alpha(1 - slideOffset).translationX(slideOffset * -inputResume.width).setDuration(0).start()

            // Animate Stats
            getAnimatorForPosition(animatorPosition)?.onSlide(slideOffset)
        }

        fun storePositionForAnimator(position: Int) {
            synchronized(lock) {
                animatorPosition = position
                animator = null
            }
        }

        fun calculateHeightForScoreView() {
            root.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    root.viewTreeObserver.removeOnPreDrawListener(this)
                    handlePreDraw()
                    return true
                }
            })
        }

        fun handlePreDraw() {
            val input = inputSheet.height
            val header = scoreHeader.height
            val footer = scoreFooter.height
            val toolbar = toolbar.height
            teamSheet.maxHeight = root.height - toolbar - header - footer - input - 100
            teamSheet.requestLayout()
        }

        private fun animateState(anim: ViewPropertyAnimator, index: Int, slideOffset: Float) {
            anim.translationY(-index * 50 * slideOffset * index).scaleX(max(0f, (1 - slideOffset * index))).alpha(1 - slideOffset).setDuration(0).start()
        }

        private fun getAnimatorForPosition(position: Int): LiveStatSlideAnimator? {
            synchronized(lock) {
                if (animator == null) {
                    animator = LiveStatSlideAnimator(pager.findViewWithTag(position), pager.findViewWithTag(position - 1), pager.findViewWithTag(position + 1))
                }
                return animator
            }
        }
    }
}
