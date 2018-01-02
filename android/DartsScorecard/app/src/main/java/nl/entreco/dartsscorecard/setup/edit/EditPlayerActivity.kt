package nl.entreco.dartsscorecard.setup.edit

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityEditPlayerBinding
import nl.entreco.dartsscorecard.di.setup.EditPlayerComponent
import nl.entreco.dartsscorecard.di.setup.EditPlayerModule

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerActivity : ViewModelActivity() {

    private val component: EditPlayerComponent by componentProvider { it.plus(EditPlayerModule(getSuggestedName())) }
    private val viewModel: EditPlayerViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityEditPlayerBinding>(this, R.layout.activity_edit_player)
        binding.viewModel = viewModel
        binding.navigator = EditPlayerNavigator(this)
        initToolbar()
    }

    private fun initToolbar() {
        setTitle(R.string.title_select_player)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun getSuggestedName(): String {
        return intent.getStringExtra("suggestion")
    }

    companion object {

        const val REQUEST_CODE = 1002

        @JvmStatic
        fun start(activity: Activity, suggestion: CharSequence) {
            val intent = Intent(activity, EditPlayerActivity::class.java)
            intent.putExtra("suggestion", suggestion)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}