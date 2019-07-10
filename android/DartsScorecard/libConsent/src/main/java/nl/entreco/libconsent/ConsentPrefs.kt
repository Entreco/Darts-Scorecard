package nl.entreco.libconsent

import android.content.SharedPreferences
import javax.inject.Inject

class ConsentPrefs @Inject constructor(
        private val prefs: SharedPreferences
) {
    fun store(status: String?, eu: Boolean?) {
        prefs.edit().apply {
            putString(Consent.Status, status)
            eu?.let {
                putBoolean(Consent.Eu, it)
            }
        }.apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun fetch(unknown: String) = Pair(prefs.getBoolean(Consent.Eu, false), prefs.getString(Consent.Status, unknown))
}