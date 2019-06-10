package nl.entreco.dartsscorecard.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.beta.donate.DonateCallback
import nl.entreco.dartsscorecard.beta.donate.DonateViewModel
import nl.entreco.dartsscorecard.databinding.ActivitySettingsBinding
import nl.entreco.dartsscorecard.di.settings.SettingsComponent
import nl.entreco.dartsscorecard.di.settings.SettingsModule
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.donations.MakeDonationResponse
import nl.entreco.libconsent.ask.AskConsentUsecase

class SettingsActivity : ViewModelActivity(), DonateCallback {

    private lateinit var binding: ActivitySettingsBinding
    private val component: SettingsComponent by componentProvider { it.plus(SettingsModule(this)) }
    private val viewModel: SettingsViewModel by viewModelProvider { component.viewModel() }
    private val donateViewModel: DonateViewModel by viewModelProvider { component.donate() }
    private val ask: AskConsentUsecase by lazy { component.ask() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.viewModel = viewModel
        binding.donations = donateViewModel

        initToolbar(toolbar(binding), R.string.title_settings)

        viewModel.ask().observe(this, Observer { consent ->
            when (consent) {
                true -> ask.askForConsent(this) {}
            }
        })
        viewModel.style().observe(this, Observer { swap ->
            viewModel.style().removeObservers(this)
            if (swap) swapStyle()
        })

        viewModel.donate().observe(this, Observer { donate ->
            if (donate) {
                donateViewModel.donations.firstOrNull()?.let { donation ->
                    donateViewModel.onDonate(donation)
                }
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

    override fun lifeCycle() = lifecycle

    override fun makeDonation(response: MakeDonationResponse) {
        donate(this, response.intent.intentSender)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            donateOk(requestCode, resultCode, data) -> donateViewModel.onMakeDonationSuccess(data)
            resultCode == Activity.RESULT_CANCELED  -> donateViewModel.onMakeDonationFailed(true)
            requestCode == REQ_CODE_DONATE          -> donateViewModel.onMakeDonationFailed(false)
            else                                    -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDonationMade(donation: Donation) {
        donateViewModel.canRemoveAds.set(false)
    }

    companion object {

        private const val REQ_CODE_DONATE = 181

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }

        @JvmStatic
        fun donate(activity: Activity, sender: IntentSender) {
            activity.startIntentSenderForResult(sender,
                    REQ_CODE_DONATE,
                    Intent(),
                    Integer.valueOf(0),
                    Integer.valueOf(0),
                    Integer.valueOf(0))
        }

        private fun donateOk(requestCode: Int, resultCode: Int, data: Intent?) =
                requestCode == REQ_CODE_DONATE && resultCode == Activity.RESULT_OK && data?.getIntExtra(
                        "RESPONSE_CODE", -1) == 0
    }
}