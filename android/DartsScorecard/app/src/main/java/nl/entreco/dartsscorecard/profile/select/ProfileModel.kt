package nl.entreco.dartsscorecard.profile.select

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 04/03/2018.
 */
data class ProfileModel(private val profile: Profile) {
    val id = profile.id
    val name = ObservableField(profile.name)
    val fav = ObservableInt(profile.prefs.favoriteDouble)
    val image = ObservableField(profile.image)
}
