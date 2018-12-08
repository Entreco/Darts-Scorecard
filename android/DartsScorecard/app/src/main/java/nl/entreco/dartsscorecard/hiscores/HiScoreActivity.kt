package nl.entreco.dartsscorecard.hiscores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityHiscoreBinding
import nl.entreco.dartsscorecard.di.hiscore.HiscoreComponent
import nl.entreco.dartsscorecard.di.hiscore.HiscoreModule

class HiScoreActivity : ViewModelActivity() {

    private val component: HiscoreComponent by componentProvider {
        it.plus(HiscoreModule(supportFragmentManager))
    }
    private val viewModel: HiScoreViewModel by viewModelProvider { component.viewModel() }
    private val adapter: HiScorePager by lazy { HiScorePager(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHiscoreBinding>(this, R.layout.activity_hiscore)
        binding.viewModel = viewModel
        binding.hiscorePager.adapter = adapter
        binding.hiscorePager.addOnPageChangeListener(object:
                ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                viewModel.updateDescription(position)
            }
        })

        initToolbar(toolbar(binding), R.string.title_hiscores)

        viewModel.hiScores().observe(this, Observer { hiscores ->
            if(hiscores.isNotEmpty()) {
                adapter.show(hiscores[0].hiScores)
            }
        })
        viewModel.title().observe(this, Observer { title ->
            setTitle(title)
        })
    }

    private fun toolbar(binding: ActivityHiscoreBinding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, HiScoreActivity::class.java)
            context.startActivity(intent)
        }
    }
}