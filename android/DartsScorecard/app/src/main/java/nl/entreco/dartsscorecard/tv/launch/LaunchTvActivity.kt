package nl.entreco.dartsscorecard.tv.launch

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityTvLaunchBinding
import nl.entreco.dartsscorecard.di.tv.TvLaunchModule

class LaunchTvActivity : ViewModelActivity() {

    private val component by componentProvider { it.plus(TvLaunchModule()) }
    private val viewModel by lazy { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityTvLaunchBinding>(this, R.layout.activity_tv_launch)
        binding.animator = LaunchTvAnimator(binding)
        binding.viewModel = viewModel
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, LaunchTvActivity::class.java)
            context.startActivity(intent)
        }
    }
}