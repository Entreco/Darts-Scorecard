package nl.entreco.dartsscorecard.profile.select

import android.databinding.ObservableBoolean
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.setup.players.*
import javax.inject.Inject

/**
 * Created by entreco on 04/03/2018.
 */
class SelectProfileViewModel @Inject constructor(
        private val createPlayerUsecase: CreatePlayerUsecase,
        private val fetchExistingPlayersUsecase: FetchExistingPlayersUsecase,
        private val deletePlayerUsecase: DeletePlayerUsecase) : BaseViewModel() {

    val isLoading = ObservableBoolean(true)
    val isEmpty = ObservableBoolean(false)

    fun fetchPlayers(adapter: SelectProfileAdapter) {
        if (adapter.itemCount <= 0) {
            reload(adapter)
        }
    }

    fun reload(adapter: SelectProfileAdapter) {
        isLoading.set(true)
        isEmpty.set(false)
        fetchExistingPlayersUsecase.exec(onFetchSuccess(adapter), onFailed())
    }

    fun create(adapter: SelectProfileAdapter, name: String, fav: Int) {
        isLoading.set(true)
        isEmpty.set(false)
        createPlayerUsecase.exec(CreatePlayerRequest(name, fav), onCreateSuccess(adapter), onFailed())
    }

    fun deletePlayerProfile(position: Int, adapter: SelectProfileAdapter) {
        val player = adapter.playerIdAt(position)
        adapter.removeAt(position)
        deletePlayerUsecase.delete(DeletePlayerRequest(player), {}, onFailed())
        reload(adapter)
    }

    private fun onFetchSuccess(adapter: SelectProfileAdapter): (FetchExistingPlayersResponse) -> Unit = { response ->
        isLoading.set(false)
        val profiles = response.players.map { Profile(it.name, it.id, it.image ?: "", it.prefs) }
        isEmpty.set(profiles.isEmpty())
        adapter.setItems(profiles)
    }

    private fun onCreateSuccess(adapter: SelectProfileAdapter): (CreatePlayerResponse) -> Unit = { _ ->
        reload(adapter)
    }

    private fun onFailed(): (Throwable) -> Unit = { _ ->
        isLoading.set(false)
        isEmpty.set(true)
    }
}
