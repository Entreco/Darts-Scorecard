package nl.entreco.dartsscorecard.beta

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.beta.votes.VoteViewModel
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule

/**
 * Created by entreco on 30/01/2018.
 */
class BetaActivity : ViewModelActivity() {

    private val component: BetaComponent by componentProvider { it.plus(BetaModule()) }
    private val viewModel: BetaViewModel by viewModelProvider { component.viewModel() }
    private val votesViewModel: VoteViewModel by viewModelProvider { component.votes() }
    private val adapter: BetaAdapter by lazy { component.adapter() }
    private lateinit var animator: BetaAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityBetaBinding>(this, R.layout.activity_beta)
        animator = BetaAnimator(binding)
        binding.viewModel = viewModel
        binding.voteViewModel = votesViewModel
        binding.animator = animator

        animator.toggler = votesViewModel
        adapter.betaAnimator = animator

        initToolbar(toolbar(binding), R.string.title_beta)
        initRecyclerView(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe(this, adapter)
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe(this)
    }

    private fun initRecyclerView(binding: ActivityBetaBinding) {
        val recyclerView = binding.betaRecyclerView
        recyclerView.layoutManager = GridLayoutManager(binding.root.context!!, 2)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
    }

    private fun toolbar(binding: ActivityBetaBinding): Toolbar {
        return binding.includeToolbar?.toolbar!!
    }

    override fun onBackPressed() {
        animator.onBackPressed() ?: super.onBackPressed()
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BetaActivity::class.java)
            context.startActivity(intent)
        }
    }
}