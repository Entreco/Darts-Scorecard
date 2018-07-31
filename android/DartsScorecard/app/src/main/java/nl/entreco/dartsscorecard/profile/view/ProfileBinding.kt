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
            when {
                path?.startsWith("team") == true -> when (path) {
                    "team0" -> view.setImageResource(R.drawable.ic_no_profile)
                    "team2" -> view.setImageResource(R.drawable.ic_team_profile2)
                    else -> view.setImageResource(R.drawable.ic_team_profile)
                }
                path?.isBlank() == true -> view.setImageResource(R.drawable.ic_no_profile)
                else -> GlideApp.with(view).load(Uri.parse(path)).into(view)
            }
        } catch (oops: NullPointerException) {
            view.setImageResource(R.drawable.ic_no_profile)
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
