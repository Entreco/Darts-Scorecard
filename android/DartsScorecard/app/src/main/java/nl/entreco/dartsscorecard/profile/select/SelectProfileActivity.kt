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
import android.view.Menu
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivitySelectProfileBinding
import nl.entreco.dartsscorecard.di.profile.SelectProfileComponent
import nl.entreco.dartsscorecard.di.profile.SelectProfileModule
import nl.entreco.dartsscorecard.profile.edit.EditPlayerNameActivity

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
        binding.navigator = navigator
        initToolbar(toolbar(binding), R.string.title_select_profile)
        initRecyclerView(binding)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPlayers(adapter)
    }

    private fun toolbar(binding: ActivitySelectProfileBinding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    private fun initRecyclerView(binding: ActivitySelectProfileBinding) {
        val recyclerView = binding.profileRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(binding.root.context!!)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isDrawingCacheEnabled = true
        addSwipeToDelete(binding, recyclerView)
        recyclerView.adapter = adapter
    }

    private fun addSwipeToDelete(binding: ActivitySelectProfileBinding, recyclerView: RecyclerView) {
        val swipeToDeleteHelper = SelectProfileSwiper(binding.root,
                onSwiped = { viewModel.hidePlayerProfile(adapter.playerIdAt(it), adapter) },
                deleteAction = { viewModel.deletePlayerProfiles(adapter) },
                undoAction = { viewModel.undoDelete(adapter) })
        swipeToDeleteHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.select, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_VIEW && resultCode == Activity.RESULT_OK) {
            viewModel.reload(adapter)
        } else if (requestCode == REQUEST_CODE_CREATE && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra(EditPlayerNameActivity.EXTRA_NAME)!!
            val double = data.getIntExtra(EditPlayerNameActivity.EXTRA_FAV, 0)
            viewModel.create(adapter, name, double)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val REQUEST_CODE_VIEW = 1111
        const val REQUEST_CODE_CREATE = 1112
        fun launch(context: Context) {
            val intent = Intent(context, SelectProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
