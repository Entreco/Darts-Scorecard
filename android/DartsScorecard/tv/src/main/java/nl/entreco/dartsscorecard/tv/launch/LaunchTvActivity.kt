package nl.entreco.dartsscorecard.tv.launch

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import nl.entreco.dartsscorecard.tv.R
import nl.entreco.dartsscorecard.tv.base.TvActivity
import nl.entreco.dartsscorecard.tv.databinding.ActivityTvLaunchBinding
import nl.entreco.dartsscorecard.tv.di.launch.LaunchTvComponent
import nl.entreco.dartsscorecard.tv.di.launch.LaunchTvModule

class LaunchTvActivity : TvActivity() {

    private val component: LaunchTvComponent by componentProvider { it.plus(LaunchTvModule()) }
    private val viewModel: LaunchTvViewModel by viewModelProvider { component.viewModel() }

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