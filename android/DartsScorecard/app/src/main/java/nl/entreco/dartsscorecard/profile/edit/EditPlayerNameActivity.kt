package nl.entreco.dartsscorecard.profile.edit

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityEditPlayerNameBinding>(this, R.layout.activity_edit_player_name)
        binding.viewModel = viewModel
        binding.animator = EditPlayerNameAnimator(binding, TransitionInflater.from(this), window)
        viewModel.playerName(intent.getStringExtra(EXTRA_NAME))
        viewModel.favouriteDouble(intent.getStringExtra(EXTRA_FAV))

        initToolbar(toolbar(binding), R.string.title_edit_player)
    }

    private fun toolbar(binding: ActivityEditPlayerNameBinding): Toolbar {
        return binding.includeToolbar?.toolbar!!
    }

    companion object {
        private const val EXTRA_NAME = "EXTRA_NAME"
        private const val EXTRA_FAV = "EXTRA_FAV"
        fun launch(activity: Activity, name: String, fav: String) : Intent {
            val intent = Intent(activity, EditPlayerNameActivity::class.java)
            intent.putExtra(EXTRA_NAME, name)
            intent.putExtra(EXTRA_FAV, fav)
            return intent
        }
    }
}
