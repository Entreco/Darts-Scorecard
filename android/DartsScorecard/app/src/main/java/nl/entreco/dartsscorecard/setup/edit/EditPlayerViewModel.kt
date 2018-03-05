package nl.entreco.dartsscorecard.setup.edit

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.players.CreatePlayerRequest
import nl.entreco.domain.setup.players.CreatePlayerResponse
import nl.entreco.domain.setup.players.CreatePlayerUsecase
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
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
                { response -> onPlayersRetrieved(response.players) },
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
        if (donePressed(action)) {
            val desiredName = view.text.toString().toLowerCase()
            val existing = allPlayers.findLast {
                it.name.toLowerCase() == desiredName
            }
            if (existing == null) {
                createPlayerUsecase.exec(CreatePlayerRequest(desiredName, 16),
                        onCreateSuccess(navigator),
                        onCreateFailed(view.rootView))
            } else {
                navigator.onSelected(existing)
            }
            return true
        }
        return false
    }

    private fun donePressed(action: Int) = action == EditorInfo.IME_ACTION_DONE

    private fun onCreateSuccess(navigator: EditPlayerNavigator): (CreatePlayerResponse) -> Unit = { response ->
        navigator.onSelected(response.player)
    }

    private fun onCreateFailed(view: View): (Throwable) -> Unit = {
        Toast.makeText(view.context, R.string.err_unable_to_update_profiler, Toast.LENGTH_SHORT).show()
    }
}
