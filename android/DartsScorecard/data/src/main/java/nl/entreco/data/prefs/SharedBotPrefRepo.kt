package nl.entreco.data.prefs

import android.content.SharedPreferences
import nl.entreco.domain.repository.BotPrefRepository

class SharedBotPrefRepo(private val prefs: SharedPreferences) : BotPrefRepository {

    companion object {
        const val PREF_BOT_SPEED: String = "PREF_bot_speed"
    }

    override fun setBotSpeed(speed: Int) = prefs.edit().apply {
        putInt(PREF_BOT_SPEED, speed)
    }.apply()

    override fun getBotSpeed(): Int = prefs.getInt(PREF_BOT_SPEED, 1250)
}