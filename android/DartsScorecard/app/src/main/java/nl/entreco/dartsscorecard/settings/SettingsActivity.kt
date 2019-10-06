package nl.entreco.dartsscorecard.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
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
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.libconsent.ask.AskConsentResponse
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
                true -> ask.askForConsent(this) { response ->
                    when (response) {
                        is AskConsentResponse.PreferPaid -> donate()
                    }
                }
            }
        })
        viewModel.style().observe(this, Observer { swap ->
            viewModel.stopStyler()
            if (swap) swapStyle()
        })

        viewModel.donate().observe(this, Observer { donate ->
            if (donate) donate()
        })
    }

    private fun donate() {
        donateViewModel.donations.firstOrNull()?.let { donation ->
            donateViewModel.onDonate(donation)
        }
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

    override fun makeDonation(response: MakePurchaseResponse) {
        handleDonation(response)
    }

    private fun handleDonation(result: MakePurchaseResponse) {
        when (result) {
            is MakePurchaseResponse.Purchased    -> donateViewModel.onMakeDonationSuccess(result)
            is MakePurchaseResponse.Acknowledged -> toast("Donation Acknowledged")
            is MakePurchaseResponse.Consumed     -> toast("Donation Consumed")
            is MakePurchaseResponse.Cancelled    -> failAndToast("Donation Cancelled")
            is MakePurchaseResponse.Error        -> failAndToast("Donation Error:$result")
            is MakePurchaseResponse.Pending      -> toast("Donation Pending - Please follow instructions")
            else -> failAndToast("Donation not confirmed - Unknown error")
        }
    }

    private fun failAndToast(message: String) {
        donateViewModel.onMakeDonationFailed(message)
        toast(message)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDonationMade(donation: Donation) {
        donateViewModel.canRemoveAds.set(false)
    }

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}