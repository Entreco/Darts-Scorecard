package nl.entreco.dartsscorecard.launch

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityLaunchBinding
import nl.entreco.dartsscorecard.di.launch.LaunchComponent
import nl.entreco.dartsscorecard.di.launch.LaunchModule


/**
 * Created by Entreco on 18/12/2017.
 */
class LaunchActivity : ViewModelActivity() {

    private val component: LaunchComponent by componentProvider { it.plus(LaunchModule()) }
    private val viewModel: LaunchViewModel by viewModelProvider { component.viewModel() }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        window.decorView.systemUiVisibility += View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.decorView.systemUiVisibility += View.SYSTEM_UI_FLAG_FULLSCREEN
        window.decorView.systemUiVisibility += View.SYSTEM_UI_FLAG_LOW_PROFILE
        window.statusBarColor = Color.BLACK
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLaunchBinding>(this, R.layout.activity_launch)
        binding.viewModel = viewModel
        binding.animator = LaunchAnimator(binding)
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