package nl.entreco.dartsscorecard.profile.select

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nl.entreco.dartsscorecard.R
import nl.entreco.dartsscorecard.databinding.ProfileViewBinding
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 04/03/2018.
 */
class SelectProfileAdapter(private val navigator: SelectProfileNavigator) : ListAdapter<Profile, ProfileView>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileView {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ProfileViewBinding>(inflater, R.layout.profile_view, parent, false)
        return ProfileView(binding)
    }

    override fun onBindViewHolder(holder: ProfileView, position: Int) {
        holder.bind(getItem(position), navigator)
    }

    fun playerIdAt(position: Int): Long {
        return if(position < itemCount) getItem(position).id else -1
    }

    fun setItems(profiles: List<Profile>) {
        submitList(profiles)
    }
}

val diff: DiffUtil.ItemCallback<Profile> = object : DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile?, newItem: Profile?): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItem: Profile?, newItem: Profile?): Boolean {
        return oldItem == newItem
    }
}
