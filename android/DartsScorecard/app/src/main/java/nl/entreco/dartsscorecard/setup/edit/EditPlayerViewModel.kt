package nl.entreco.dartsscorecard.setup.edit

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.usecase.CreatePlayerRequest
import nl.entreco.domain.setup.usecase.CreatePlayerUsecase
import nl.entreco.domain.setup.usecase.FetchExistingPlayersUsecase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerViewModel @Inject constructor(private val createPlayerUsecase: CreatePlayerUsecase,
                                              @Named("suggestion") suggestedName: String,
                                              fetchExistingPlayersUsecase: FetchExistingPlayersUsecase)
    : BaseViewModel() {

    val filteredPlayers = ObservableArrayList<Player>()
    val suggestedName = ObservableField<String>(suggestedName)
    private val allPlayers = emptyList<Player>().toMutableList()

    init {
        fetchExistingPlayersUsecase.exec(
                { players -> onPlayersRetrieved(players) },
                { onPlayersFailed() }
        )
    }

    private fun onPlayersFailed() {
        allPlayers.clear()
        filteredPlayers.clear()
    }

    private fun onPlayersRetrieved(players: List<Player>) {
        allPlayers.addAll(players)
        filteredPlayers.addAll(players)
    }

    fun filter(text: CharSequence) {
        val filter = allPlayers.filter { it.name.toLowerCase().startsWith(text.toString().toLowerCase()) }
        filteredPlayers.clear()
        filteredPlayers.addAll(filter)
    }

    fun onActionDone(view: TextView, action: Int, navigator: EditPlayerNavigator): Boolean {
        if (action == EditorInfo.IME_ACTION_DONE) {
            createPlayerUsecase.exec(CreatePlayerRequest(view.text.toString(), 16),
                    onCreateSuccess(navigator, view),
                    onCreateFailed())
            return true
        }
        return false
    }

    private fun onCreateSuccess(navigator: EditPlayerNavigator, view: TextView): (Player) -> Unit =
            { player -> navigator.onSelected(view, player) }

    private fun onCreateFailed(): (Throwable) -> Unit = { }
}