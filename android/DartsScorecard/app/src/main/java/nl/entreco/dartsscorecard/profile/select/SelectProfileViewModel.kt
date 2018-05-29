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

    fun reload(adapter: SelectProfileAdapter, playerToHide: Long? = null) {
        isLoading.set(true)
        isEmpty.set(false)
        fetchExistingPlayersUsecase.exec(onFetchSuccess(adapter, playerToHide), onFailed())
    }

    fun create(adapter: SelectProfileAdapter, name: String, fav: Int) {
        isLoading.set(true)
        isEmpty.set(false)
        createPlayerUsecase.exec(CreatePlayerRequest(name, fav), onCreateSuccess(adapter), onFailed())
    }

    fun deletePlayerProfile(player: Long, adapter: SelectProfileAdapter) {
        isLoading.set(true)
        deletePlayerUsecase.delete(DeletePlayerRequest(player), { reload(adapter) }, onFailed())
    }

    fun hidePlayerProfile(player: Long, adapter: SelectProfileAdapter) {
        isLoading.set(true)
        reload(adapter, player)
    }

    private fun onFetchSuccess(adapter: SelectProfileAdapter, playerToHide: Long?): (FetchExistingPlayersResponse) -> Unit = { response ->
        isLoading.set(false)
        val profiles = if (playerToHide != null) {
            toProfiles(response).filter { it.id == playerToHide }
        } else {
            toProfiles(response)
        }
        isEmpty.set(profiles.isEmpty())
        adapter.setItems(profiles)
    }

    private fun toProfiles(response: FetchExistingPlayersResponse) =
            response.players.map { Profile(it.name, it.id, it.image ?: "", it.prefs) }

    private fun onCreateSuccess(adapter: SelectProfileAdapter): (CreatePlayerResponse) -> Unit = { _ ->
        reload(adapter)
    }

    private fun onFailed(): (Throwable) -> Unit = { _ ->
        isLoading.set(false)
        isEmpty.set(true)
    }
}
