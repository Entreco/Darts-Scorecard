package nl.entreco.dartsscorecard.setup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import nl.entreco.dartsscorecard.R
import nl.entreco.libads.ui.AdViewModel
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivitySetup01Binding
import nl.entreco.dartsscorecard.di.setup.Setup01Component
import nl.entreco.dartsscorecard.di.setup.Setup01Module
import nl.entreco.dartsscorecard.setup.players.PlayersViewModel
import nl.entreco.dartsscorecard.setup.settings.SettingsViewModel

/**
 * Created by Entreco on 20/12/2017.
 */
class Setup01Activity : ViewModelActivity() {

    private val component: Setup01Component by componentProvider { it.plus(Setup01Module(this)) }
    private val viewModel: Setup01ViewModel by viewModelProvider { component.viewModel() }
    private val playersViewModel: PlayersViewModel by viewModelProvider { component.players() }
    private val settingsViewModel: SettingsViewModel by viewModelProvider { component.settings() }
    private val adViewModel: AdViewModel by viewModelProvider { component.ads() }
    private val navigator: Setup01Navigator by lazy { Setup01Navigator(this) }
    private var initialLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySetup01Binding>(this, R.layout.activity_setup_01)
        binding.viewModel = viewModel
        binding.playersViewModel = playersViewModel
        binding.adViewModel = adViewModel
        binding.settingsViewModel = settingsViewModel
        binding.navigator = navigator

        initToolbar(toolbar(binding), R.string.title_setup)

        if (savedInstanceState == null) {
            initialLaunch = true
            navigator.onAddNewPlayer(0, emptyList(), emptyList())
        }
    }

    private fun toolbar(binding: ActivitySetup01Binding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_CANCELED && initialLaunch){
            finish()
            return
        }
        initialLaunch = false
        navigator.handleResult(requestCode, resultCode, data, playersViewModel.adapter)
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, Setup01Activity::class.java)
            context.startActivity(intent)
        }
    }
}
