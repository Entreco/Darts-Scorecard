package nl.entreco.dartsscorecard.profile

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.View
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.ViewModelActivity
import nl.entreco.dartsscorecard.databinding.ActivityProfileBinding
import nl.entreco.dartsscorecard.di.profile.ProfileComponent
import nl.entreco.dartsscorecard.di.profile.ProfileModule

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

        initAppbar(binding.includeAppbar!!.profileAppbar, binding.includeAppbar.toolbarHeaderView!!.toolbarHeaderView)
    }

    private var isHideToolbarView = false
    private fun initAppbar(appbar: AppBarLayout, toolbarHeaderView: View) {
        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / maxScroll.toFloat()

            if (percentage == 1f && isHideToolbarView) {
                toolbarHeaderView.visibility = View.VISIBLE
                isHideToolbarView = !isHideToolbarView

            } else if (percentage < 1f && !isHideToolbarView) {
                toolbarHeaderView.visibility = View.GONE
                isHideToolbarView = !isHideToolbarView
            }
        }
    }

    companion object {
        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}
