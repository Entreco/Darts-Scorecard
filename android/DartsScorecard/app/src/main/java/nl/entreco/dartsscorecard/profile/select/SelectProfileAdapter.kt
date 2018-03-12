package nl.entreco.dartsscorecard.profile.select

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.base.TestableAdapter
import nl.entreco.dartsscorecard.databinding.ProfileViewBinding
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 04/03/2018.
 */
class SelectProfileAdapter(private val navigator: SelectProfileNavigator) : TestableAdapter<ProfileView>() {

    private val items: MutableList<Profile> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProfileViewBinding>(inflater, R.layout.profile_view, parent, false)
        return ProfileView(binding)
    }

    override fun onBindViewHolder(holder: ProfileView, position: Int) {
        holder.bind(items[position], navigator)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(profiles: List<Profile>) {
        items.clear()
        items.addAll(profiles)
        tryNotifyItemRangeChanged(0, profiles.size)
    }
}
