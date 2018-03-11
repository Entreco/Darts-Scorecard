package nl.entreco.dartsscorecard.profile.select

import android.databinding.ObservableBoolean
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.setup.players.FetchExistingPlayersResponse
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
import javax.inject.Inject

/**
 * Created by entreco on 04/03/2018.
 */
class SelectProfileViewModel @Inject constructor(private val fetchExistingPlayersUsecase: FetchExistingPlayersUsecase) : BaseViewModel() {

    val isLoading = ObservableBoolean(true)
    val isEmpty = ObservableBoolean(false)

    fun fetchPlayers(adapter: SelectProfileAdapter) {
        if (adapter.itemCount <= 0) {
            fetchExistingPlayersUsecase.exec(onFetchSuccess(adapter), onFetchFailed())
        }
    }

    private fun onFetchSuccess(adapter: SelectProfileAdapter): (FetchExistingPlayersResponse) -> Unit = { response ->
        isLoading.set(false)
        val profiles = response.players.map { Profile(it.name, it.id, it.image ?: "", it.prefs) }
        isEmpty.set(profiles.isEmpty())
        adapter.setItems(profiles)
    }

    private fun onFetchFailed(): (Throwable) -> Unit = { _ ->
        isLoading.set(false)
        isEmpty.set(true)
    }
}
