package nl.entreco.dartsscorecard.profile

import android.databinding.BindingAdapter
import android.net.Uri
import android.widget.ImageView
import nl.entreco.dartsscorecard.R

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
                    val uri = Uri.parse(path)
//                    view.context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    view.setImageURI(uri)
//                    Picasso.with(view.context).load(uri)
//                            .error(R.drawable.ic_no_profile)
//                            .resize(100, 100)
//                            .centerCrop()
//                            .into(view)
                }
            } catch (oops: Exception) {
                view.setImageResource(R.drawable.ic_no_profile)
            }
        }
    }
}
