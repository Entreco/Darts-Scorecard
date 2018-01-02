package nl.entreco.dartsscorecard.setup.edit

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.FetchExistingPlayersUsecase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerViewModel @Inject constructor(@Named("suggestion") suggestedName: String,
                                              fetchExistingPlayersUsecase: FetchExistingPlayersUsecase)
    : BaseViewModel() {

    val existingPlayers = ObservableArrayList<Player>()
    val suggestedName = ObservableField<String>(suggestedName)

    init {
        fetchExistingPlayersUsecase.exec(
                { players -> onPlayersRetrieved(players) },
                { onPlayersFailed() }
        )
    }

    private fun onPlayersFailed() {
        existingPlayers.clear()
    }

    private fun onPlayersRetrieved(players: List<Player>) {
        existingPlayers.addAll(players)
    }

    fun onActionDone(view: TextView, action: Int, navigator: EditPlayerNavigator): Boolean {
        if (action == EditorInfo.IME_ACTION_DONE) {
            navigator.onSelected(view, Player(view.text.toString()))
            return true
        }
        return false
    }
}