package nl.entreco.dartsscorecard.launch

import android.databinding.BindingAdapter
import android.view.View
import nl.entreco.domain.setup.game.CreateGameResponse

/**
 * Created by Entreco on 19/12/2017.
 */
abstract class LaunchBindings {

    companion object {
        @JvmStatic
        @BindingAdapter("resumeGame")
        fun resumeGame(view: View, response: CreateGameResponse?) {
            view.animate().alpha(if (response == null) 0.5F else 1.0F).start()
        }
    }
}