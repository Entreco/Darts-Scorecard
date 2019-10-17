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
import nl.entreco.dartsscorecard.beta.donate.DonateViewModel
import nl.entreco.dartsscorecard.databinding.ActivitySettingsBinding
import nl.entreco.dartsscorecard.di.settings.SettingsComponent
import nl.entreco.dartsscorecard.di.settings.SettingsModule
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.libconsent.ask.AskConsentResponse
import nl.entreco.libconsent.ask.AskConsentUsecase

class SettingsActivity : ViewModelActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val component: SettingsComponent by componentProvider {
        it.plus(SettingsModule(this) { response ->
            when (response) {
                is MakePurchaseResponse.Updated     -> donateViewModel.onUpdate(response.purchases)
                is MakePurchaseResponse.Purchased   -> donateViewModel.onDonated(response.donation, response.orderId)
                is MakePurchaseResponse.Donations   -> donateViewModel.showDonations(response.donations)
                is MakePurchaseResponse.Unavailable -> donateViewModel.showDonations(emptyList())
                is MakePurchaseResponse.Cancelled   -> donateViewModel.onCancelled()
            }
        })
    }
    private val viewModel: SettingsViewModel by viewModelProvider { component.viewModel() }
    private val donateViewModel: DonateViewModel by viewModelProvider { component.donate() }
    private val billingService: BillingRepo by lazy { component.billing() }
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

        billingService.start()
        viewModel.donate().observe(this, Observer { donate ->
            if (donate) donate()
        })

    }

    override fun onResume() {
        super.onResume()
        billingService.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        billingService.stop()
    }

    private fun donate() {
        donateViewModel.donations.firstOrNull()?.let { donation ->
            donateViewModel.onDonate(donation)
            billingService.purchase(donation.sku)
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

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}