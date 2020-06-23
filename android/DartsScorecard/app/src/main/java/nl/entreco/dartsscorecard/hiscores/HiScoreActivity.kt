package nl.entreco.dartsscorecard.hiscores

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.PagerAnimator
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityHiscoreBinding
import nl.entreco.dartsscorecard.di.hiscore.HiscoreComponent
import nl.entreco.dartsscorecard.di.hiscore.HiscoreModule

class HiScoreActivity : ViewModelActivity(), HiscoreComponentProvider {

    private lateinit var binding: ActivityHiscoreBinding
    private val component: HiscoreComponent by componentProvider { it.plus(HiscoreModule(this)) }
    override fun provide() = component

    private val viewModel: HiScoreViewModel by viewModelProvider { component.viewModel() }
    private val pageAdapter: HiScorePager by lazy { component.pager() }
    private val animator: PagerAnimator by lazy {
        PagerAnimator(binding.hiscorePager, binding.hiscorePrev, binding.hiscoreNext)
    }
    private val navigator by lazy { component.navigator() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hiscore)
        binding.viewModel = viewModel
        binding.animator = animator
        binding.hiscorePager.adapter = pageAdapter
        binding.hiscorePager.addOnPageChangeListener(object :
                ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                viewModel.updateDescription(position)
                animator.onPageSelected(position)
            }
        })

        initToolbar(toolbar(binding), R.string.title_hiscores)

        viewModel.hiScores().observe(this, Observer { hiscores ->
            if (hiscores.isNotEmpty()) {
                pageAdapter.show(hiscores[0].hiScore)
            }
        })

        viewModel.events().observe(this, navigator)
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