package nl.entreco.data.setup.repository

import android.content.SharedPreferences
import nl.entreco.domain.repository.PreferenceRepository
import nl.entreco.domain.setup.usecase.FetchSettingsResponse

/**
 * Created by entreco on 04/01/2018.
 */
class SharedPreferenceRepo(private val prefs: SharedPreferences) : PreferenceRepository {

    companion object {
        const val PREF_SETS: String = "PREF_sets"
        const val PREF_LEGS: String = "PREF_legs"
        const val PREF_MIN: String = "PREF_min"
        const val PREF_MAX: String = "PREF_max"
        const val PREF_SCORE: String = "PREF_score"
    }

    override fun fetchPreferredSetup(): FetchSettingsResponse {
        val sets = prefs.getInt(PREF_SETS, FetchSettingsResponse.def_sets)
        val legs = prefs.getInt(PREF_LEGS, FetchSettingsResponse.def_legs)
        val min = prefs.getInt(PREF_MIN, FetchSettingsResponse.def_min)
        val max = prefs.getInt(PREF_MAX, FetchSettingsResponse.def_max)
        val score = prefs.getInt(PREF_SCORE, FetchSettingsResponse.def_start)
        return FetchSettingsResponse(sets, legs, min, max, score)
    }
}