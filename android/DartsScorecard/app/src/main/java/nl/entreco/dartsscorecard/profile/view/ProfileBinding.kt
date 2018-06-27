package nl.entreco.dartsscorecard.profile.view

import android.databinding.BindingAdapter
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.di.glide.GlideApp

/**
 * Created by entreco on 23/02/2018.
 */

object ProfileBinding {
    @JvmStatic
    @BindingAdapter("profileImage")
    fun loadProfileImage(view: ImageView, path: String?) {
        try {
            val uri = Uri.parse(path)
            if (uri == null || uri.toString().isBlank()) {
                view.setImageResource(R.drawable.ic_no_profile)
            } else {
                GlideApp.with(view).load(uri).into(view)
            }
        } catch (oops: NullPointerException) {
            view.setImageResource(R.drawable.ic_no_profile)
        }
    }

    @JvmStatic
    @BindingAdapter("profileDouble")
    fun showProfileDouble(view: TextView, favDouble: String?) {
        try {
            val double = view.context.resources.getStringArray(R.array.fav_doubles)[favDouble?.toInt()!!]
            view.text = view.context.getString(R.string.favourite_double, double)
        } catch (oops: Exception) {
            val double = view.context.resources.getStringArray(R.array.fav_doubles)[0]
            view.text = view.context.getString(R.string.favourite_double, double)
        }
    }

    @JvmStatic
    @BindingAdapter("profileDouble")
    fun showProfileDoubleInt(view: TextView, favDouble: Int?) {
        try {
            val double = view.context.resources.getStringArray(R.array.fav_doubles)[favDouble!!]
            view.text = view.context.getString(R.string.favourite_double, double)
        } catch (oops: Exception) {
            val double = view.context.resources.getStringArray(R.array.fav_doubles)[0]
            view.text = view.context.getString(R.string.favourite_double, double)
        }
    }
}
