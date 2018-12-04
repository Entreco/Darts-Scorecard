package nl.entreco.dartsscorecard.setup.edit

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityEditPlayerBinding
import nl.entreco.dartsscorecard.di.setup.EditPlayerComponent
import nl.entreco.dartsscorecard.di.setup.EditPlayerModule
import nl.entreco.dartsscorecard.setup.Setup01Navigator

/**
 * Created by Entreco on 02/01/2018.
 */
class EditPlayerActivity : ViewModelActivity() {

    private val component: EditPlayerComponent by componentProvider { it.plus(EditPlayerModule(getSuggestedName(), otherPlayers())) }
    private val viewModel: EditPlayerViewModel by viewModelProvider { component.viewModel() }
    private val navigator: EditPlayerNavigator by lazy { EditPlayerNavigator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityEditPlayerBinding>(this, R.layout.activity_edit_player)
        binding.viewModel = viewModel
        binding.navigator = navigator
        initToolbar(toolbar(binding), R.string.title_select_player)
    }

    private fun getSuggestedName(): String {
        return intent.getStringExtra(Setup01Navigator.EXTRA_SUGGESTION)
    }

    private fun otherPlayers(): LongArray {
        return intent.getLongArrayExtra(Setup01Navigator.EXTRA_OTHER_PLAYERS)
    }

    private fun toolbar(binding: ActivityEditPlayerBinding): Toolbar {
        return binding.includeToolbar.toolbar
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

    override fun onBackPressed() {
        navigator.onBackPressed()
    }
}