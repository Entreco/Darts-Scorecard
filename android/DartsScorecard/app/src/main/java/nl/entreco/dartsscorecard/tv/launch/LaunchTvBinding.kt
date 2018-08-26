package nl.entreco.dartsscorecard.tv.launch

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup

object LaunchTvBinding {

    @JvmStatic
    @BindingAdapter("attachMatch", "launchNavigator")
    fun attachMatchFragmentToContainer(view: ViewGroup, attach: Boolean, navigator: LaunchTvNavigator){
        if(attach){
            navigator.attach()
        } else {
            navigator.detach()
        }
    }
}