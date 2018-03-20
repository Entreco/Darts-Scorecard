package nl.entreco.dartsscorecard.profile.create

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityCreateProfileBinding
import nl.entreco.dartsscorecard.di.profile.CreateProfileComponent
import nl.entreco.dartsscorecard.di.profile.CreateProfileModule
import nl.entreco.dartsscorecard.profile.select.SelectProfileActivity

/**
 * Created by entreco on 20/03/2018.
 */
class CreateProfileActivity : ViewModelActivity() {

    private val component: CreateProfileComponent by componentProvider { it.plus(CreateProfileModule()) }
    private val viewModel: CreateProfileViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCreateProfileBinding>(this, R.layout.activity_create_profile)
        binding.viewModel = viewModel
        initToolbar(toolbar(binding), R.string.title_create_profile)
    }

    private fun toolbar(binding: ActivityCreateProfileBinding): Toolbar {
        return binding.includeToolbar?.toolbar!!
    }

    companion object {
        @JvmStatic
        fun launch(activity: Activity, view: View) {
            val intent = Intent(activity, CreateProfileActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivityForResult(intent, SelectProfileActivity.REQUEST_CODE_CREATE, options.toBundle())
        }
    }
}