package nl.entreco.dartsscorecard.setup.edit

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.players.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerViewModel @Inject constructor(private val createPlayerUsecase: CreatePlayerUsecase,
                                              @Named("otherPlayers") private val otherPlayers: LongArray,
                                              @Named("otherBots") private val otherBots: LongArray,
                                              @Named("suggestion") suggestedName: String,
                                              fetchExistingPlayersUsecase: FetchExistingPlayersUsecase,
                                              fetchBotsUsecase: FetchBotsUsecase)
    : BaseViewModel() {

    val filteredPlayers = ObservableArrayList<Player>()
    val availableBots = ObservableArrayList<Bot>()
    val suggestedName = ObservableField(suggestedName)
    val errorMsg = ObservableInt()
    private val allPlayers = emptyList<Player>().toMutableList()
    private val allBots = emptyList<Bot>().toMutableList()

    init {
        fetchExistingPlayersUsecase.exec(
                { response -> onPlayersRetrieved(response.players) },
                { onPlayersFailed() }
        )

        if(suggestedName.startsWith("Player")) {
            fetchBotsUsecase.exec(
                    { response -> onBotsRetrieved(response.bots) },
                    { onBotsFailed() })
        }
    }

    private fun onPlayersFailed() {
        errorMsg.set(R.string.err_unable_to_fetch_players)
        allPlayers.clear()
        filteredPlayers.clear()
    }

    private fun onPlayersRetrieved(players: List<Player>) {
        val remainingPlayers = players.filterNot { otherPlayers.contains(it.id) }
        allPlayers.addAll(players)
        filteredPlayers.addAll(remainingPlayers)
        filter("")
    }

    private fun onBotsFailed(){
        allBots.clear()
    }

    private fun onBotsRetrieved(bots: List<Bot>) {
        val remainingPlayers = bots.filterNot { otherBots.contains(it.id) }
        allBots.addAll(bots)
        availableBots.addAll(remainingPlayers)
    }

    fun filter(text: CharSequence) {
        val keep = addPlayersWhosNameStartsWith(text)
        val typos = addPlayersWhosNameContains(text)
        removeOthers(keep, typos)
    }

    private fun addPlayersWhosNameStartsWith(text: CharSequence): List<Player> {
        val keep = allPlayers.filter { it.name.toLowerCase().startsWith(text.toString().toLowerCase()) && !otherPlayers.contains(it.id) }
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
        val typos = allPlayers.filter { it.name.toLowerCase().contains(text.toString().toLowerCase()) && !otherPlayers.contains(it.id) }
        typos.forEach {
            if (!filteredPlayers.contains(it)) {
                filteredPlayers.add(it)
            }
        }
        return typos
    }

    private fun removeOthers(keep: List<Player>, typos: List<Player>) {
        val remove = allPlayers.filter { !keep.contains(it) && !typos.contains(it) && !otherPlayers.contains(it.id) }
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

            when {
                isNewPlayer(existing) -> createPlayerUsecase.exec(CreatePlayerRequest(desiredName),
                        onCreateSuccess(navigator),
                        onCreateFailed())
                isAlreadyPlaying(existing!!, desiredName) -> errorMsg.set(R.string.err_player_already_in_match)
                else -> navigator.onSelected(existing)
            }
            return true
        }
        return false
    }

    private fun isAlreadyPlaying(existing: Player, desiredName: String) = otherPlayers.contains(existing.id) && suggestedName.get() != desiredName.toLowerCase()
    private fun isNewPlayer(existing: Player?) = existing == null
    private fun donePressed(action: Int) = action == EditorInfo.IME_ACTION_DONE

    private fun onCreateSuccess(navigator: EditPlayerNavigator): (CreatePlayerResponse) -> Unit = { response ->
        navigator.onSelected(response.player)
    }

    private fun onCreateFailed(): (Throwable) -> Unit = {
        when(it){
            is PlayerAlreadyExistsException -> errorMsg.set(R.string.err_player_already_exists)
            is InvalidPlayerNameException -> errorMsg.set(R.string.err_invalid_player_name)
            else -> errorMsg.set(R.string.err_unable_to_create_player)
        }
    }
}
