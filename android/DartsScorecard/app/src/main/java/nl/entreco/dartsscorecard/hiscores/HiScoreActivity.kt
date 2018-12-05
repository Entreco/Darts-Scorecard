package nl.entreco.dartsscorecard.hiscores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityHiscoreBinding
import nl.entreco.dartsscorecard.di.hiscore.HiscoreComponent
import nl.entreco.dartsscorecard.di.hiscore.HiscoreModule

class HiScoreActivity : ViewModelActivity() {

    private val component: HiscoreComponent by componentProvider { it.plus(HiscoreModule()) }
    private val viewModel: HiScoreViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHiscoreBinding>(this, R.layout.activity_hiscore)
        binding.viewModel = viewModel
        binding.hiscorePager.adapter = HiScorePager(supportFragmentManager)
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, HiScoreActivity::class.java)
            context.startActivity(intent)
        }
    }
}