package nl.entreco.dartsscorecard.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivitySettingsBinding
import nl.entreco.dartsscorecard.di.settings.SettingsComponent
import nl.entreco.dartsscorecard.di.settings.SettingsModule
import nl.entreco.libconsent.ask.AskConsentUsecase

class SettingsActivity : ViewModelActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val component: SettingsComponent by componentProvider { it.plus(SettingsModule()) }
    private val viewModel: SettingsViewModel by viewModelProvider { component.viewModel() }
    private val ask: AskConsentUsecase by lazy { component.ask() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.viewModel = viewModel

        initToolbar(toolbar(binding), R.string.title_settings)
    }

    override fun onResume() {
        super.onResume()
        viewModel.style().observe(this, Observer {
            swapStyle()
        })
        viewModel.ask().observe(this, Observer { consent ->
            when (consent) {
                true -> ask.askForConsent(this) {}
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toolbar(binding: ActivitySettingsBinding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}