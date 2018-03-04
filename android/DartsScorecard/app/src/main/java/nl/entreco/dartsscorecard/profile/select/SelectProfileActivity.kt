package nl.entreco.dartsscorecard.profile.select

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivitySelectProfileBinding
import nl.entreco.dartsscorecard.di.profile.SelectProfileComponent
import nl.entreco.dartsscorecard.di.profile.SelectProfileModule

/**
 * Created by entreco on 04/03/2018.
 */
class SelectProfileActivity : ViewModelActivity() {

    private val component: SelectProfileComponent by componentProvider { it.plus(SelectProfileModule()) }
    private val viewModel: SelectProfileViewModel by viewModelProvider { component.viewModel() }
    private val adapter: SelectProfileAdapter by lazy { SelectProfileAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySelectProfileBinding>(this, R.layout.activity_select_profile)
        binding.viewModel = viewModel
        initToolbar(toolbar(binding), R.string.title_select_profile)
        initRecyclerView(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlayers(adapter)
    }

    private fun toolbar(binding: ActivitySelectProfileBinding): Toolbar {
        return binding.includeToolbar?.toolbar!!
    }

    private fun initRecyclerView(binding: ActivitySelectProfileBinding) {
        val recyclerView = binding.profileRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context!!)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    companion object {
        fun launch(context: Context){
            val intent = Intent(context, SelectProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
