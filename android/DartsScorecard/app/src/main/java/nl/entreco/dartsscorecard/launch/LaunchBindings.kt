package nl.entreco.dartsscorecard.launch

import android.databinding.BindingAdapter
import android.view.View
import nl.entreco.domain.setup.game.CreateGameResponse

/**
 * Created by Entreco on 19/12/2017.
 */

object LaunchBindings {
    
    private const val half: Float = 0.5F
    private const val full: Float = 1.0F

    @JvmStatic
    @BindingAdapter("resumeGame")
    fun resumeGame(view: View, response: CreateGameResponse?) {
        view.animate().alpha(if (response == null) half else full).start()
    }
}