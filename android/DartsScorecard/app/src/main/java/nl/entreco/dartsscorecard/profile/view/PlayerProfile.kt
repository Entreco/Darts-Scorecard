package nl.entreco.dartsscorecard.profile.view

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 23/02/2018.
 */
data class PlayerProfile(private val profile: Profile) {
    val id = profile.id
    val fav = ObservableInt(profile.prefs.favoriteDouble)
    val name = ObservableField<String>(profile.name)
    val image = ObservableField<String>(profile.image)
}
