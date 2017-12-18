package nl.entreco.dartsscorecard.launch

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityLaunchBinding
import nl.entreco.dartsscorecard.di.launch.LaunchComponent
import nl.entreco.dartsscorecard.di.launch.LaunchModule
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest

/**
 * Created by Entreco on 18/12/2017.
 */
class LaunchActivity : ViewModelActivity() {

    private val component: LaunchComponent by componentProvider { it.plus(LaunchModule()) }
    private val viewModel: LaunchViewModel by viewModelProvider { component.viewModel() }

    private val setup = CreateGameRequest(501, 0, 3, 2)
    private val teams = TeamNamesString("Remco,Boeffie|Eva,Guusje")

    private var launch : RetrieveGameRequest? = null
    private var clicked : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLaunchBinding>(this, R.layout.activity_launch)
        binding.launchPlay.setOnClickListener {
            clicked = true
            tryLaunch(clicked, launch)
        }

        viewModel.createFrom(teams, setup, startPlayActivity(), showError())
    }

    private fun tryLaunch(clicked: Boolean, launch: RetrieveGameRequest?) {
        if(clicked && launch != null) {
            Play01Activity.startGame(this, launch)
        }
    }

    private fun showError(): (Throwable) -> Unit {
        return { err ->
            Toast.makeText(this,
                    "Oops ${err.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun startPlayActivity(): (RetrieveGameRequest) -> Unit {
        return { setup ->
            launch = setup
            tryLaunch(clicked, setup)
        }
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, LaunchActivity::class.java)
            context.startActivity(intent)
        }
    }
}