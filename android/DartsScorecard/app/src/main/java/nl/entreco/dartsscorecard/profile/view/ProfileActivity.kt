package nl.entreco.dartsscorecard.profile.view

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.di.profile.ProfileComponent
import nl.entreco.dartsscorecard.di.profile.ProfileModule
import nl.entreco.dartsscorecard.profile.edit.EditPlayerNameActivity
import nl.entreco.dartsscorecard.profile.select.SelectProfileActivity


/**
 * Created by entreco on 21/02/2018.
 */
class ProfileActivity : ViewModelActivity() {

    private val component: ProfileComponent by componentProvider { it.plus(ProfileModule()) }
    private val viewModel: ProfileViewModel by viewModelProvider { component.viewModel() }
    private var madeChanges = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile)
        binding.viewModel = viewModel
        binding.animator = ProfileAnimator(binding, TransitionInflater.from(this), window)
        binding.navigator = ProfileNavigator(this)

        viewModel.fetchProfile(idsFromIntent(intent))

        initToolbar(toolbar(binding), R.string.empty)
    }

    private fun toolbar(binding: ActivityProfileBinding): Toolbar {
        return binding.includeAppbar.toolbar
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CHANGE_IMAGE && resultCode == Activity.RESULT_OK) {
            madeChanges = true
            viewModel.showImageForProfile(data, resources.getDimension(R.dimen.header_profile_pic_size))
        } else if (requestCode == REQUEST_CODE_CHANGE_NAME && resultCode == Activity.RESULT_OK) {
            madeChanges = true
            viewModel.showNameForProfile(data?.getStringExtra(EditPlayerNameActivity.EXTRA_NAME)!!, data.getIntExtra(EditPlayerNameActivity.EXTRA_FAV, 0))
        }
        super.onActivityResult(requestCode, resultCode, data)
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
        if (madeChanges) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    companion object {
        private const val EXTRA_TEAM_IDS = "EXTRA_TEAM_IDS"
        private const val REQUEST_CODE_CHANGE_IMAGE = 1222
        private const val REQUEST_CODE_CHANGE_NAME = 1223

        @JvmStatic
        fun launch(activity: Activity, view: View, teams: LongArray) {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra(EXTRA_TEAM_IDS, teams)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivityForResult(intent, SelectProfileActivity.REQUEST_CODE_VIEW, options.toBundle())
        }

        @JvmStatic
        fun selectImage(activity: Activity) {
            val pickPhoto = Intent(Intent.ACTION_PICK)
            pickPhoto.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickPhoto.type = "image/*"
            activity.startActivityForResult(pickPhoto, REQUEST_CODE_CHANGE_IMAGE)
        }

        @JvmStatic
        fun selectName(activity: Activity, name: String, fav: Int) {
            val view = activity.findViewById<TextView>(R.id.profileHeaderName)
            val pickName = EditPlayerNameActivity.launch(activity, name, activity.resources.getStringArray(R.array.fav_doubles)[fav])
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivityForResult(pickName, REQUEST_CODE_CHANGE_NAME, options.toBundle())//one can be replaced with any action code
        }

        fun idsFromIntent(intent: Intent): LongArray {
            return intent.getLongArrayExtra(EXTRA_TEAM_IDS)
        }
    }
}
