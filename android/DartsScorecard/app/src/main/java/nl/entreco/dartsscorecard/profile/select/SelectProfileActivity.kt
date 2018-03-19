package nl.entreco.dartsscorecard.profile.select

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.SwipeToDeleteCallback
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
    private val navigator: SelectProfileNavigator by lazy { SelectProfileNavigator(this) }
    private val adapter: SelectProfileAdapter by lazy { SelectProfileAdapter(navigator) }

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
        val swipeToDeleteHelper = ItemTouchHelper(object : SwipeToDeleteCallback(binding.root.context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeAt(position)
                viewModel.deletePlayerProfile(adapter.playerIdAt(position), adapter)
            }
        })
        swipeToDeleteHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_VIEW && resultCode == Activity.RESULT_OK) {
            viewModel.reload(adapter)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val REQUEST_CODE_VIEW = 1111
        fun launch(context: Context) {
            val intent = Intent(context, SelectProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
