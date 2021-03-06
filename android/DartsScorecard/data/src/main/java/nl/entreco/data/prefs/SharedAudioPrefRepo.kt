package nl.entreco.data.prefs

import android.content.SharedPreferences
import nl.entreco.domain.repository.AudioPrefRepository

/**
 * Created by entreco on 14/03/2018.
 */
class SharedAudioPrefRepo(private val prefs: SharedPreferences) : AudioPrefRepository {

    companion object {
        const val PREF_MASTER_CALLER: String = "PREF_mastercaller"
        const val PREF_BACKGROUND_MUSIC: String = "PREF_background"
    }

    override fun isMasterCallerEnabled(): Boolean {
        return prefs.getBoolean(PREF_MASTER_CALLER, false)
    }

    override fun setMasterCallerEnabled(enabled: Boolean) {
        prefs.edit().apply {
            putBoolean(PREF_MASTER_CALLER, enabled)
        }.apply()
    }

    override fun isBackgroundMusicEnabled(): Boolean {
        return prefs.getBoolean(PREF_BACKGROUND_MUSIC, false)
    }

    override fun setBackgroundMusicEnabled(enabled: Boolean) {
        prefs.edit().apply {
            putBoolean(PREF_BACKGROUND_MUSIC, enabled)
        }.apply()
    }
}