package nl.entreco.dartsscorecard.launch

import android.databinding.BindingAdapter
import android.view.View
import nl.entreco.domain.repository.RetrieveGameRequest

/**
 * Created by Entreco on 19/12/2017.
 */
class LaunchBindings {

    companion object {
        @JvmStatic
        @BindingAdapter("resumeGame")
        fun resumeGame(view: View, request: RetrieveGameRequest?){
            view.animate().alpha(if(request == null) 0.5F else 1.0F).start()
        }
    }
}