package nl.entreco.dartsscorecard.play

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import nl.entreco.dartsscorecard.R

/**
 * Created by entreco on 25/03/2018.
 */
object Play01Binding {
    @JvmStatic
    @BindingAdapter("loading")
    fun showLoading(view: ViewGroup, loading: Boolean) {
        if (loading) {
            val loadingView = LayoutInflater.from(view.context)
                    .inflate(R.layout.play_01_loading, view, false)
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
        if (msg != 0) {
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