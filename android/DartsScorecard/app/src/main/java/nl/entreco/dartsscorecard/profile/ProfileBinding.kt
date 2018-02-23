package nl.entreco.dartsscorecard.profile

import android.databinding.BindingAdapter
import android.net.Uri
import android.widget.ImageView

/**
 * Created by entreco on 23/02/2018.
 */
class ProfileBinding {

    companion object {
        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadProfileImage(view: ImageView, path: String?) {
            try {
                if (path != null && path.isNotBlank()) {
                    view.setImageURI(Uri.parse(path))
                }
            } catch (oops: Exception) {
            }
        }
    }
}
