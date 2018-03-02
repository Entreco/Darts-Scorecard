package nl.entreco.dartsscorecard.profile

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.transition.TransitionInflater
import android.view.View
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.di.profile.ProfileComponent
import nl.entreco.dartsscorecard.di.profile.ProfileModule
import nl.entreco.dartsscorecard.profile.edit.EditPlayerNameActivity
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
        binding.navigator = ProfileNavigator(this)
        viewModel.fetchProfile(idsFromIntent(intent))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CHANGE_IMAGE && resultCode == Activity.RESULT_OK) {
            viewModel.showImageForProfile(data, resources.getDimension(R.dimen.header_profile_pic_size))
        } else if (requestCode == REQUEST_CODE_CHANGE_NAME && resultCode == Activity.RESULT_OK) {
            viewModel.showNameForProfile(data?.getStringExtra(EXTRA_UPDATED_NAME)!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val EXTRA_UPDATED_NAME = "EXTRA_UPDATED_NAME"
        private const val EXTRA_TEAM_IDS = "EXTRA_TEAM_IDS"
        private const val REQUEST_CODE_CHANGE_IMAGE = 1222
        private const val REQUEST_CODE_CHANGE_NAME = 1223

        @JvmStatic
        fun launch(activity: Activity, view: View, team: Team) {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra(EXTRA_TEAM_IDS, team.players.map { it.id }.toLongArray())

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivity(intent, options.toBundle())
        }

        @JvmStatic
        fun selectImage(activity: Activity) {
            val pickPhoto = Intent(Intent.ACTION_PICK)
            pickPhoto.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickPhoto.type = "image/*"
            activity.startActivityForResult(pickPhoto, REQUEST_CODE_CHANGE_IMAGE)//one can be replaced with any action code
        }

        @JvmStatic
        fun selectName(activity: Activity, name: String, fav: String) {
            val view = activity.findViewById<TextView>(R.id.profileHeaderName)
            val pickName = EditPlayerNameActivity.launch(activity, name, fav)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivityForResult(pickName, REQUEST_CODE_CHANGE_NAME, options.toBundle())//one can be replaced with any action code
        }

        fun idsFromIntent(intent: Intent): LongArray {
            return intent.getLongArrayExtra(EXTRA_TEAM_IDS)
        }
    }
}
