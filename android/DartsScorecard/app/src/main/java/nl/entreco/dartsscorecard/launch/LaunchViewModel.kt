package nl.entreco.dartsscorecard.launch

import android.content.Context
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.dartsscorecard.setup.Setup01Activity
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.launch.usecase.RetrieveLatestGameUsecase
import nl.entreco.domain.repository.RetrieveGameRequest
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class LaunchViewModel @Inject constructor(private val retrieveGameUsecase: RetrieveLatestGameUsecase) : BaseViewModel() {

    val resumedGame = ObservableField<RetrieveGameRequest?>(null)

    fun onNewGamePressed(context: Context) {
        Setup01Activity.launch(context)
    }

    fun onResumePressed(context: Context) {
        val request = resumedGame.get()
        if (request != null) {
            Play01Activity.startGame(context, request)
        }
    }

    fun retrieveLatestGame() {
        retrieveGameUsecase.exec(setGameToResume(), removeGameToResume())
    }

    private fun removeGameToResume(): (Throwable) -> Unit = { resumedGame.set(null) }

    private fun setGameToResume(): (FetchLatestGameResponse) -> Unit {
        return { (gameId, teamIds, gameRequest) ->
            resumedGame.set(RetrieveGameRequest(gameId, teamIds, gameRequest))
        }
    }
}