package nl.entreco.data.prefs

import android.content.SharedPreferences
import nl.entreco.domain.repository.RatingPrefRepository

class SharedRatingPrefRepo(private val prefs: SharedPreferences) : RatingPrefRepository {

    companion object {
        const val PREF_SHOULD_ASK_RATING: String = "PREF_rating"
    }

    override fun neverAskAgain() {
        prefs.edit().apply{
            putBoolean(PREF_SHOULD_ASK_RATING, false)
        }.apply()
    }

    override fun shouldAskToRateApp(): Boolean {
        return prefs.getBoolean(PREF_SHOULD_ASK_RATING, true)
    }
}