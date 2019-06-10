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
import nl.entreco.libads.Ads


/**
 * Created by Entreco on 18/12/2017.
 */
class LaunchActivity : ViewModelActivity() {

    private val component: LaunchComponent by componentProvider { it.plus(LaunchModule()) }
    private val viewModel: LaunchViewModel by viewModelProvider { component.viewModel() }
    private val adViewModel by viewModelProvider { component.adViewModel() }
    private val ads: Ads by lazy { component.ads() }
//    private val ask: AskConsentUsecase by lazy { component.ask() }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.BLACK
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLaunchBinding>(this, R.layout.activity_launch)
        binding.viewModel = viewModel
        binding.animator = LaunchAnimator(binding)
        adViewModel.consent().observe(this, Observer { consent ->
            when (consent) {
                true  -> {
                }
                false -> ads.init(getString(R.string.app_id))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.retrieveLatestGame()
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, LaunchActivity::class.java)
            context.startActivity(intent)
        }
    }
}