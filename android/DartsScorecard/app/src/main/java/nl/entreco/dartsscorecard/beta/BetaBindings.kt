package nl.entreco.dartsscorecard.beta

import android.databinding.BindingAdapter
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by entreco on 03/02/2018.
 */
object BetaBindings {

    private const val duration: Long = 1000
    private const val startDelay: Long = 350

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            Picasso.get().load(Uri.parse(it)).fit().centerCrop().into(view)
        }
    }


    @JvmStatic
    @BindingAdapter("progress")
    fun animateProgress(view: View, oldProgress: Float, progress: Float) {
        view.pivotX = 0F
        view.scaleX = oldProgress
        view.animate().scaleX(progress).setDuration(duration).setStartDelay(startDelay).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    @BindingAdapter("textWithTags")
    fun setTextWithTags(view: TextView, message: String?) {
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
            } else {
                view.text = Html.fromHtml(message)
            }
        }
    }
}
