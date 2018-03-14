package nl.entreco.dartsscorecard.profile.select

import android.support.v7.widget.RecyclerView
import nl.entreco.dartsscorecard.databinding.ProfileViewBinding
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 04/03/2018.
 */
class ProfileView(private val binding: ProfileViewBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(profile: Profile, navigator: SelectProfileNavigator) {
        binding.profile = ProfileModel(profile)
        binding.navigator = navigator
        binding.executePendingBindings()
    }
}
