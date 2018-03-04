package nl.entreco.dartsscorecard.profile.view

import android.databinding.ObservableField
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 23/02/2018.
 */
class PlayerProfile(profile: Profile) {
    val id = profile.id
    val fav = ObservableField<String>("${profile.prefs.favoriteDouble}")
    val name = ObservableField<String>(profile.name)
    val image = ObservableField<String>(profile.image)
}
