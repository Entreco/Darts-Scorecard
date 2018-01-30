package nl.entreco.dartsscorecard.beta

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityBetaBinding
import nl.entreco.dartsscorecard.di.beta.BetaAdapter
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule

/**
 * Created by entreco on 30/01/2018.
 */
class BetaActivity : ViewModelActivity() {

    private val component: BetaComponent by componentProvider { it.plus(BetaModule()) }
    private val viewModel: BetaViewModel by viewModelProvider { component.viewModel() }
    private val adapter: BetaAdapter by lazy { component.adapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityBetaBinding>(this, R.layout.activity_beta)
        binding.viewModel = viewModel

        initToolbar(toolbar(binding), R.string.title_beta)
        initRecyclerView(binding)
    }

    private fun initRecyclerView(binding: ActivityBetaBinding) {
        val recyclerView = binding.betaRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        recyclerView.adapter = adapter
    }

    private fun toolbar(binding: ActivityBetaBinding): Toolbar {
        return binding.includeToolbar?.toolbar!!
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BetaActivity::class.java)
            context.startActivity(intent)
        }
    }
}