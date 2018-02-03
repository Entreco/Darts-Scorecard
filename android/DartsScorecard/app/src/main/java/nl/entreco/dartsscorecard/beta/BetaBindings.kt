package nl.entreco.dartsscorecard.beta

import android.databinding.BindingAdapter
import android.net.Uri
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
            Picasso.with(view.context)
                    .load(Uri.parse(url))
                    .into(view)
        }
    }
}