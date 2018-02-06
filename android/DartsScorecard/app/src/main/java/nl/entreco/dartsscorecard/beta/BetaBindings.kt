package nl.entreco.dartsscorecard.beta

import android.databinding.BindingAdapter
import android.net.Uri
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by entreco on 03/02/2018.
 */
class BetaBindings {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            url?.let {
                Picasso.with(view.context)
                        .load(Uri.parse(it))
                        .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("progress")
        fun animateProgress(view: View, oldProgress: Float, progress: Float) {
            view.pivotX = 0F
            view.scaleX = oldProgress
            view.animate().scaleX(progress).setDuration(1000).setStartDelay(350).setInterpolator(AccelerateDecelerateInterpolator()).start()
        }
    }
}