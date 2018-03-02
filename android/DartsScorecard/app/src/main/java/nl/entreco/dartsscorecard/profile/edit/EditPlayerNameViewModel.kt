package nl.entreco.dartsscorecard.profile.edit

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Handler
import nl.entreco.dartsscorecard.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameViewModel @Inject constructor(private val handler: Handler) : BaseViewModel() {

    val isTyping = ObservableBoolean(false)
    val name = ObservableField<String>()

    fun playerName(playerName: String?) {
        name.set(playerName)
        handler.postDelayed({
            isTyping.set(true)
        }, 500)
    }

    fun favouriteDouble(fav: String) {
        // TODO: Show Favourite Double
    }

}
