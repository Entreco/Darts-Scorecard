package nl.entreco.dartsscorecard.splash

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.play.model.Game
import nl.entreco.domain.play.usecase.CreateGameUsecase
import nl.entreco.domain.play.usecase.SetupModel


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : ViewModelActivity(), CreateGameUsecase.Callback {

    private val component: SplashComponent by componentProvider { it.plus(SplashModule()) }
    private val viewModel: SplashViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val setup = SetupModel(501, 0, 3, 2)
        viewModel.createGameIfNoneExists(setup, this)
    }

    override fun onGameCreated(game: Game, setup: SetupModel) {
        Play01Activity.startGame(this, game, setup)
        finish()
    }

    override fun onGameFailed(err: Throwable) {
        Log.d("WOW", "doh: ${err.localizedMessage}")
        Toast.makeText(this, "Oops ${err.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}