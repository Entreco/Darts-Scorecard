package nl.entreco.dartsscorecard.launch

import android.content.Context
import android.databinding.ObservableField
import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.dartsscorecard.beta.BetaActivity
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.dartsscorecard.profile.select.SelectProfileActivity
import nl.entreco.dartsscorecard.setup.Setup01Activity
import nl.entreco.domain.launch.FetchLatestGameResponse
import nl.entreco.domain.launch.RetrieveLatestGameUsecase
import nl.entreco.domain.setup.game.CreateGameResponse
import javax.inject.Inject

/**
 * Created by Entreco on 12/12/2017.
 */
class LaunchViewModel @Inject constructor(private val retrieveGameUsecase: RetrieveLatestGameUsecase) : BaseViewModel() {

    val resumedGame = ObservableField<CreateGameResponse?>()

    fun onNewGamePressed(context: Context) {
        Setup01Activity.launch(context)
    }

    fun onResumePressed(context: Context) {
        val request = resumedGame.get()
        if (request != null) {
            Play01Activity.startGame(context, request)
        }
    }

    fun onProfilePressed(context: Context) {
        SelectProfileActivity.launch(context)
    }

    fun onBetaPressed(context: Context) {
        BetaActivity.launch(context)
    }

    fun retrieveLatestGame() {
        retrieveGameUsecase.exec(setGameToResume(), removeGameToResume())
    }

    private fun removeGameToResume(): (Throwable) -> Unit = { resumedGame.set(null) }

    private fun setGameToResume(): (FetchLatestGameResponse) -> Unit {
        return { (gameId, teamIds, startScore, startIndex, numLegs, numSets) ->
            resumedGame.set(CreateGameResponse(gameId, teamIds, startScore, startIndex, numLegs, numSets))
        }
    }
}
