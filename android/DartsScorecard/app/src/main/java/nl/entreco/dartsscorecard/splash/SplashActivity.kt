package nl.entreco.dartsscorecard.splash

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.model.players.TeamsString
import nl.entreco.domain.play.usecase.CreateGameInput
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.CreateTeamsUsecase


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : ViewModelActivity(), CreateGameUsecase.Callback, CreateTeamsUsecase.Callback {

    private val component: SplashComponent by componentProvider { it.plus(SplashModule()) }
    private val viewModel: SplashViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val teams = TeamsString("Remco,Sibbel|Boeffie|Eva,Guusje,Beer")
        viewModel.createTeamsIfNoneExists(teams, this)
    }

    override fun onTeamsCreated(teams: TeamsString) {
        val setup = CreateGameInput(501, 0, 3, 2)
        viewModel.createGameIfNoneExists(setup, teams, this)
    }

    override fun onGameCreated(game: Game, setup: CreateGameInput) {
        Play01Activity.startGame(this, game, setup)
        finish()
    }

    override fun onTeamsFailed(err: Throwable) {
        err(err)
    }

    override fun onGameFailed(err: Throwable) {
        err(err)
    }

    private fun err(err: Throwable) {
        Log.d("WOW", "doh: ${err.localizedMessage}")
        Toast.makeText(this, "Oops ${err.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}