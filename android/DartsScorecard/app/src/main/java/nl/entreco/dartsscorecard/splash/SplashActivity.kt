package nl.entreco.dartsscorecard.splash

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.di.splash.SplashComponent
import nl.entreco.dartsscorecard.di.splash.SplashModule
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.play.model.players.TeamIdsString
import nl.entreco.domain.play.model.players.TeamNamesString
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : ViewModelActivity(), CreateGameUsecase.Callback, CreateTeamsUsecase.Callback {

    private val component: SplashComponent by componentProvider { it.plus(SplashModule()) }
    private val viewModel: SplashViewModel by viewModelProvider { component.viewModel() }

    private val setup = GameSettingsRequest(501, 0, 3, 2)
    private val teams = TeamNamesString("Remco,Boeffie|Eva,Guusje")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.ensureTeamPlayersExist(teams, this)
    }

    override fun onTeamsFailed(err: Throwable) {
        err(err)
    }

    override fun onTeamsCreated(teamIds: TeamIdsString) {
        viewModel.retrieveLastGame(setup, teamIds, this)
    }

    override fun onGameRetrieved(setup: RetrieveGameRequest) {
        start(setup)
    }

    override fun onGameRetrieveFailed(err: Throwable, teamIds: TeamIdsString) {
        viewModel.createNewGame(setup, teamIds, this)
    }

    override fun onGameCreated(setup: RetrieveGameRequest) {
        start(setup)
    }

    override fun onGameCreateFailed(err: Throwable) {
        err(err)
    }

    private fun start(setup: RetrieveGameRequest) {
        Play01Activity.startGame(this, setup)
        finish()
    }

    private fun err(err: Throwable) {
        Log.d("WOW", "doh: ${err.localizedMessage}")
        Toast.makeText(this, "Oops ${err.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}