package nl.entreco.dartsscorecard.faq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.yalantis.jellytoolbar.listener.JellyListener
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityWtfBinding
import nl.entreco.dartsscorecard.databinding.SearchbarBinding
import nl.entreco.dartsscorecard.di.faq.WtfComponent
import nl.entreco.dartsscorecard.di.faq.WtfModule


class WtfActivity : ViewModelActivity() {

    private lateinit var binding: ActivityWtfBinding
    private lateinit var searchBinding: SearchbarBinding
    private val component: WtfComponent by componentProvider { it.plus(WtfModule()) }
    private val viewModel: WtfViewModel by viewModelProvider { component.viewModel() }
    private val adapter: WtfAdapter by lazy { component.adapter() }

    private val jellyListener = object : JellyListener() {
        override fun onCancelIconClicked() {
            val searchField = searchBinding.searchField
            if (searchField.text?.isEmpty() == true) {
                binding.includeToolbar.toolbar.collapse()
                val imm = getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchField.windowToken, 0)
            } else {
                searchField.text?.clear()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_wtf)
        binding.viewModel = viewModel

        initJellyBar()
        initToolbar(toolbar(binding), R.string.title_wtf)
        initRecyclerView(binding)
    }

    private fun initJellyBar() {
        binding.includeToolbar.toolbar.jellyListener = jellyListener
        searchBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.searchbar, null,
                false)
        searchBinding.listener = adapter
        binding.includeToolbar.toolbar.contentView = searchBinding.root
    }

    private fun initRecyclerView(binding: ActivityWtfBinding) {
        val recyclerView = binding.wtfRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                binding.root.context, 1)
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    private fun toolbar(binding: ActivityWtfBinding): Toolbar {
        return binding.includeToolbar.toolbar.toolbar!!
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe(this, adapter)
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, WtfActivity::class.java)
            context.startActivity(intent)
        }
    }
}