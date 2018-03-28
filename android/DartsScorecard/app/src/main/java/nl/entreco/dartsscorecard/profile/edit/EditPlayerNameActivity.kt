package nl.entreco.dartsscorecard.profile.edit

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityEditPlayerNameBinding
import nl.entreco.dartsscorecard.di.profile.EditPlayerNameComponent
import nl.entreco.dartsscorecard.di.profile.EditPlayerNameModule

/**
 * Created by entreco on 02/03/2018.
 */
class EditPlayerNameActivity : ViewModelActivity() {

    private val component: EditPlayerNameComponent by componentProvider { it.plus(EditPlayerNameModule()) }
    private val viewModel: EditPlayerNameViewModel by viewModelProvider { component.viewModel() }
    private val navigator: EditPlayerNameNavigator by lazy { EditPlayerNameNavigator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityEditPlayerNameBinding>(this, R.layout.activity_edit_player_name)
        binding.animator = EditPlayerNameAnimator(binding, TransitionInflater.from(this), window)
        binding.viewModel = viewModel
        binding.window = window
        binding.navigator = navigator
        initToolbar(toolbar(binding), title())

        viewModel.playerName(intent.getStringExtra(EXTRA_NAME), intent.getStringExtra(EXTRA_FAV), this)
    }

    private fun toolbar(binding: ActivityEditPlayerNameBinding): Toolbar {
        return binding.includeToolbar.toolbar
    }

    @StringRes
    private fun title() : Int {
        return if(intent.getBooleanExtra(EXTRA_CREATE, false)){
            R.string.title_create_profile
        } else {
            R.string.title_edit_profile
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> navigator.onCancel()
            R.id.menu_edit_done -> viewModel.onDone(navigator)
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_FAV = "EXTRA_FAV"
        const val EXTRA_CREATE = "EXTRA_CREATE"
        fun launch(activity: Activity, name: String, fav: String): Intent {
            val intent = Intent(activity, EditPlayerNameActivity::class.java)
            intent.putExtra(EXTRA_NAME, name)
            intent.putExtra(EXTRA_FAV, fav)
            intent.putExtra(EXTRA_CREATE, false)
            return intent
        }

        fun launch(activity: Activity): Intent {
            val intent = Intent(activity, EditPlayerNameActivity::class.java)
            intent.putExtra(EXTRA_NAME, "")
            intent.putExtra(EXTRA_FAV, "")
            intent.putExtra(EXTRA_CREATE, true)
            return intent
        }

        fun result(desiredName: String, desiredDouble: Int): Intent {
            val intent = Intent()
            intent.putExtra(EXTRA_NAME, desiredName)
            intent.putExtra(EXTRA_FAV, desiredDouble)
            return intent
        }
    }
}
