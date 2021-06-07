package nl.entreco.dartsscorecard.profile.edit

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
import javax.inject.Inject

private const val ERR_EMPTY = 0
private const val ERR_DUPLICATE = 1
private const val NAME_OK = 2

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameViewModel @Inject constructor(
    private val handler: Handler,
    private val analytics: Analytics,
    fetchExistingPlayersUsecase: FetchExistingPlayersUsecase,
) : BaseViewModel() {

    val isTyping = ObservableBoolean(false)
    val name = ObservableField("")
    val favDouble = ObservableField("")
    val favDoubleIndex = ObservableInt()
    val errorMsg = ObservableInt()
    internal lateinit var initialProfileName: String
    internal val allPlayers = emptyList<Player>().toMutableList()

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
        initialProfileName = playerName.lowercase()
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
            ERR_EMPTY
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
            name.set(view.text.toString().lowercase())
            onDone(navigator)
            return true
        }
        return false
    }

    fun onDone(navigator: EditPlayerNameNavigator): Boolean {
        isTyping.set(false)

        val desiredDouble = favDoubleIndex.get()
        val desiredName = name.get()!!.lowercase()
        val existing = allPlayers.findLast {
            it.name.lowercase() == desiredName
        }

        return when (isValidName(existing, desiredName)) {
            NAME_OK -> navigator.onDoneEditing(desiredName, desiredDouble)
            ERR_EMPTY -> handleError(R.string.err_player_name_is_empty)
            ERR_DUPLICATE -> handleError(R.string.err_player_already_exists)
            else -> handleError(R.string.err_unable_to_create_player)
        }
    }

    private fun handleError(@StringRes msg: Int): Boolean {
        isTyping.set(true)
        errorMsg.set(msg)
        return true
    }

    private fun isValidName(existing: Player?, desiredName: String): Int {
        if (desiredName.isEmpty()) return ERR_EMPTY
        else if (existing != null && desiredName != initialProfileName) return ERR_DUPLICATE
        return NAME_OK
    }

    private fun donePressed(action: Int) = action == EditorInfo.IME_ACTION_DONE
}
