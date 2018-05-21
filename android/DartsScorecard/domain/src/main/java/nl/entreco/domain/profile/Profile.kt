package nl.entreco.domain.profile

import nl.entreco.domain.model.players.PlayerPrefs

/**
 * Created by entreco on 23/02/2018.
 */
data class Profile(val name: String, val id: Long = 0, val image: String, val prefs: PlayerPrefs = PlayerPrefs(16))
