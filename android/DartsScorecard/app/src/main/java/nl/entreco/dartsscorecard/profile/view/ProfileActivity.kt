package nl.entreco.dartsscorecard.profile.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.di.profile.ProfileComponent
import nl.entreco.dartsscorecard.di.profile.ProfileModule
import nl.entreco.dartsscorecard.profile.edit.EditPlayerNameActivity
import nl.entreco.dartsscorecard.profile.select.SelectProfileActivity
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.BillingRepo
import nl.entreco.libads.ui.AdViewModel


/**
 * Created by entreco on 21/02/2018.
 */
class ProfileActivity : ViewModelActivity() {

    private val component: ProfileComponent by componentProvider {
        it.plus(ProfileModule(this) { response ->
            if (response is MakePurchaseResponse.Updated) adViewModel.onPurchasesRetrieved(response)
        })
    }
    private val viewModel: ProfileViewModel by viewModelProvider { component.viewModel() }
    private val adViewModel: AdViewModel by viewModelProvider { component.ads() }
    private val navigator: ProfileNavigator by lazy { ProfileNavigator(this) }
    private val billing: BillingRepo by lazy { component.billing() }
    private var madeChanges = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityProfileBinding>(this, R.layout.activity_profile)
        binding.viewModel = viewModel
        binding.animator = ProfileAnimator(binding, TransitionInflater.from(this), window)
        binding.navigator = navigator
        binding.adViewModel = adViewModel
        viewModel.fetchProfile(idsFromIntent(intent))
        billing.start()
        initToolbar(binding.mainToolbar, R.string.empty, true)
    }

    override fun onResume() {
        super.onResume()
        billing.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        billing.stop()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        menu?.findItem(R.id.menu_profile_edit)?.isVisible = idsFromIntent(intent).size <= 1
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home      -> {
                onBackPressed()
                true
            }
            R.id.menu_profile_edit -> {
                navigator.onEditProfile(viewModel.profile.get()!!)
                true
            }
            else                   -> super.onOptionsItemSelected(item)
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
        fun launch(activity: Activity, teams: LongArray, view: View? = null) {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra(EXTRA_TEAM_IDS, teams)

            val options = if (view != null) ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName) else null
            activity.startActivityForResult(intent, SelectProfileActivity.REQUEST_CODE_VIEW, options?.toBundle())
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
            val view = activity.findViewById<TextView>(R.id.mainTextviewTitle)
            val pickName = EditPlayerNameActivity.launch(activity, name, activity.resources.getStringArray(R.array.fav_doubles)[fav])
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            activity.startActivityForResult(pickName, REQUEST_CODE_CHANGE_NAME, options.toBundle())//one can be replaced with any action code
        }

        fun idsFromIntent(intent: Intent): LongArray {
            return intent.getLongArrayExtra(EXTRA_TEAM_IDS) ?: longArrayOf()
        }
    }
}
