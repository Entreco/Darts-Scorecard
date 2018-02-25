package nl.entreco.dartsscorecard.profile

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.transition.TransitionInflater
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.di.profile.ProfileComponent
import nl.entreco.dartsscorecard.di.profile.ProfileModule
import nl.entreco.domain.model.players.Team


/**
 * Created by entreco on 21/02/2018.
 */
class ProfileActivity : ViewModelActivity() {

    private val component: ProfileComponent by componentProvider { it.plus(ProfileModule()) }
    private val viewModel: ProfileViewModel by viewModelProvider { component.viewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile)
        binding.viewModel = viewModel
        binding.animator = ProfileAnimator(binding, TransitionInflater.from(this), window)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchProfile(idsFromIntent(intent))
    }

    companion object {
        private const val EXTRA_TEAM_IDS = "EXTRA_TEAM_IDS"
        @JvmStatic
        fun launch(activity: Activity, view: View, team: Team) {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra(EXTRA_TEAM_IDS, team.players.map { it.id }.toLongArray())

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivity(intent, options.toBundle())
        }

        fun idsFromIntent(intent: Intent): LongArray {
            return intent.getLongArrayExtra(EXTRA_TEAM_IDS)
        }
    }
}
