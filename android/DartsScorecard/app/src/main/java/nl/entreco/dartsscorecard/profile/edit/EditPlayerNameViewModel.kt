package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
import javax.inject.Inject

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameViewModel @Inject constructor(private val handler: Handler,
                                                  private val analytics: Analytics,
                                                  fetchExistingPlayersUsecase: FetchExistingPlayersUsecase) : BaseViewModel() {

    val isTyping = ObservableBoolean(false)
    val name = ObservableField<String>()
    val favDouble = ObservableField<String>()
    val favDoubleIndex = ObservableInt()
    val errorMsg = ObservableInt()
    private lateinit var initialProfileName: String
    private val allPlayers = emptyList<Player>().toMutableList()

    init {
        fetchExistingPlayersUsecase.exec(
                { response -> onPlayersRetrieved(response.players) },
                { onPlayersFailed() }
        )
    }

    private fun onPlayersFailed() {
        allPlayers.clear()
        errorMsg.set(R.string.err_unable_to_fetch_players)
    }

    private fun onPlayersRetrieved(players: List<Player>) {
        allPlayers.addAll(players)
    }

    fun playerName(playerName: String, fav: String, context: Context) {
        initialProfileName = playerName
        favDouble.set(fav)
        favDoubleIndex.set(toIndex(fav, context))
        name.set(playerName)
        handler.postDelayed({
            isTyping.set(true)
        }, 500)
    }

    fun onNameChanged(editable: Editable) {
        name.set(editable.toString())
    }

    private fun toIndex(fav: String, context: Context): Int {
        return try {
            context.resources.getStringArray(R.array.fav_doubles).indexOf(fav)
        } catch (unknownFavDouble: Exception) {
            0
        }
    }

    fun onFavouriteDoubleSelected(adapter: AdapterView<*>, index: Int) {
        val resolved = adapter.getItemAtPosition(index).toString()
        favDoubleIndex.set(index)
        favDouble.set(resolved)
        analytics.setFavDoubleProperty(resolved)
    }

    fun onActionDone(view: TextView, action: Int, navigator: EditPlayerNameNavigator): Boolean {
        if (donePressed(action)) {
            name.set(view.text.toString().toLowerCase())
            onDone(navigator)
            return true
        }
        return false
    }

    fun onDone(navigator: EditPlayerNameNavigator): Boolean {
        isTyping.set(false)

        val desiredDouble = favDoubleIndex.get()
        val desiredName = name.get().toLowerCase()
        val existing = allPlayers.findLast {
            it.name.toLowerCase() == desiredName
        }
        if (existing == null || desiredName == initialProfileName) {
            return navigator.onDoneEditing(desiredName, desiredDouble)
        } else {
            isTyping.set(true)
            errorMsg.set(R.string.err_player_already_exists)
        }
        return false
    }

    private fun donePressed(action: Int) = action == EditorInfo.IME_ACTION_DONE
}
