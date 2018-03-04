package nl.entreco.dartsscorecard.profile.select

import android.databinding.ObservableField
import android.databinding.ObservableInt
import nl.entreco.domain.profile.Profile

/**
 * Created by entreco on 04/03/2018.
 */
class ProfileModel (profile: Profile){
    val name = ObservableField<String>(profile.name)
    val fav = ObservableInt(profile.prefs.favoriteDouble)
    val image = ObservableField<String>(profile.image)
}
