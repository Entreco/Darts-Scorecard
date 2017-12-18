package nl.entreco.dartsscorecard.splash

import android.os.Bundle
import android.widget.Toast
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.di.splash.SplashComponent
import nl.entreco.dartsscorecard.di.splash.SplashModule
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.splash.TeamNamesString
import nl.entreco.domain.play.usecase.GameSettingsRequest
import nl.entreco.domain.play.usecase.RetrieveGameRequest


/**
 * Created by Entreco on 12/12/2017.
 */
class SplashActivity : ViewModelActivity() {

    private val component: SplashComponent by componentProvider { it.plus(SplashModule()) }
    private val viewModel: SplashViewModel by viewModelProvider { component.viewModel() }

    private val setup = GameSettingsRequest(501, 0, 3, 2)
    private val teams = TeamNamesString("Remco,Boeffie|Eva,Guusje")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.createFrom(teams, setup, startPlayActivity(), showError())
    }

    private fun showError(): (Throwable) -> Unit {
        return { err ->
            Toast.makeText(this,
                    "Oops ${err.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun startPlayActivity(): (RetrieveGameRequest) -> Unit {
        return { setup ->
            Play01Activity.startGame(this, setup)
            finish()
        }
    }
}