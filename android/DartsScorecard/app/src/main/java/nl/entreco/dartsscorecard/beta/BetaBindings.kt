package nl.entreco.dartsscorecard.beta

import android.databinding.BindingAdapter
import android.net.Uri
import android.widget.ImageView

/**
 * Created by entreco on 03/02/2018.
 */
class BetaBindings {

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?){

            view.setImageURI(Uri.parse(url))
        }
    }
}