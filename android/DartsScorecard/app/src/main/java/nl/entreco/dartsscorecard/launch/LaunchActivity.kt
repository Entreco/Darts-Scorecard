package nl.entreco.dartsscorecard.launch

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityLaunchBinding
import nl.entreco.dartsscorecard.di.launch.LaunchComponent
import nl.entreco.dartsscorecard.di.launch.LaunchModule
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.libads.Ads
import nl.entreco.libconsent.ask.AskConsentResponse
import nl.entreco.libconsent.ask.AskConsentUsecase


/**
 * Created by Entreco on 18/12/2017.
 */
class LaunchActivity : ViewModelActivity() {

    private val component: LaunchComponent by componentProvider {
        it.plus(LaunchModule(this) { response ->
            if(response is MakePurchaseResponse.Updated) adViewModel.onPurchasesRetrieved(response)
        })
    }
    private val viewModel: LaunchViewModel by viewModelProvider { component.viewModel() }
    private val adViewModel by viewModelProvider { component.adViewModel() }
    private val ads: Ads by lazy { component.ads() }
    private val billing: BillingRepo by lazy { component.billing() }
    private val ask: AskConsentUsecase by lazy { component.ask() }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.BLACK
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLaunchBinding>(this, R.layout.activity_launch)
        binding.viewModel = viewModel
        binding.animator = LaunchAnimator(binding)
        billing.start()
        adViewModel.consent().observe(this, Observer { consent ->
            when (consent) {
                true  -> ask.askForConsent(this) { response ->
                    if (response is AskConsentResponse.PreferPaid) {
                        viewModel.onSettingsPressed(this)
                    }
                }
                false -> ads.init()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveLatestGame()
        billing.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        billing.stop()
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, LaunchActivity::class.java)
            context.startActivity(intent)
        }
    }
}