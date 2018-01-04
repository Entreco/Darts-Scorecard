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
        val keep = addPlayersWhosNameStartsWith(text)
        val typos = addPlayersWhosNameContains(text)
        removeOthers(keep, typos)
    }

    private fun addPlayersWhosNameStartsWith(text: CharSequence): List<Player> {
        val keep = allPlayers.filter { it.name.toLowerCase().startsWith(text.toString().toLowerCase()) }
        keep.forEach {
            if (!filteredPlayers.contains(it)) {
                filteredPlayers.add(0, it)
            } else {
                filteredPlayers.remove(it)
                filteredPlayers.add(0, it)
            }
        }
        return keep
    }

    private fun addPlayersWhosNameContains(text: CharSequence): List<Player> {
        val typos = allPlayers.filter { it.name.toLowerCase().contains(text.toString().toLowerCase()) }
        typos.forEach {
            if (!filteredPlayers.contains(it)) {
                filteredPlayers.add(it)
            }
        }
        return typos
    }

    private fun removeOthers(keep: List<Player>, typos: List<Player>) {
        val remove = allPlayers.filter { !keep.contains(it) && !typos.contains(it) }
        remove.forEach {
            if (filteredPlayers.contains(it)) {
                filteredPlayers.remove(it)
            }
        }
    }

    fun onActionDone(view: TextView, action: Int, navigator: EditPlayerNavigator): Boolean {
        if (action == EditorInfo.IME_ACTION_DONE) {
            val existing = allPlayers.findLast { it.name.toLowerCase() == view.text.toString() }
            if (existing == null) {
                createPlayerUsecase.exec(CreatePlayerRequest(view.text.toString(), 16),
                        onCreateSuccess(navigator),
                        onCreateFailed())
            } else {
                navigator.onSelected(existing)
            }
            return true
        }
        return false
    }

    private fun onCreateSuccess(navigator: EditPlayerNavigator): (Player) -> Unit =
            { player -> navigator.onSelected(player) }

    private fun onCreateFailed(): (Throwable) -> Unit = { }
}