package nl.entreco.dartsscorecard.base

import android.view.View
import android.view.ViewTreeObserver
import androidx.viewpager.widget.ViewPager

class PagerAnimator(private val pager: ViewPager, private val prev: View, private val next: View) {

    init {
        pager.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                pager.viewTreeObserver.removeOnPreDrawListener(this)
                onPageSelected(0)
                return true
            }
        })
    }

    fun onPageSelected(position: Int) {
        when {
            position <= 0 -> flyOutStart(prev)
            position + 1 >= pager.adapter?.count ?: 0 -> flyOutEnd(next)
            else -> {
                flyInStart(prev)
                flyInEnd(next)
            }
        }
    }

    private fun flyOutStart(view: View) {
        view.animate().alpha(0F).translationX(-view.width.toFloat()).scaleX(0F).scaleY(0F).start()
    }

    private fun flyInStart(view: View) {
        view.animate().alpha(1F).translationX(0F).scaleX(1F).scaleY(1F).start()
    }

    private fun flyOutEnd(view: View) {
        view.animate().alpha(0F).translationX(view.width.toFloat()).scaleX(0F).scaleY(0F).start()
    }

    private fun flyInEnd(view: View) {
        view.animate().alpha(1F).translationX(0F).scaleX(1F).scaleY(1F).start()
    }
}
