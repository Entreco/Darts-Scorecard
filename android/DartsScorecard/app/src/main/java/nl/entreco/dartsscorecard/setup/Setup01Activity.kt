package nl.entreco.dartsscorecard.setup

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivitySetup01Binding
import nl.entreco.dartsscorecard.di.setup.Setup01Component
import nl.entreco.dartsscorecard.di.setup.Setup01Module

/**
 * Created by Entreco on 20/12/2017.
 */
class Setup01Activity : ViewModelActivity() {

    private val component: Setup01Component by componentProvider { it.plus(Setup01Module()) }
    private val viewModel: Setup01ViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySetup01Binding>(this, R.layout.activity_setup_01)
        binding.viewModel = viewModel
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, Setup01Activity::class.java)
            context.startActivity(intent)
        }
    }
}