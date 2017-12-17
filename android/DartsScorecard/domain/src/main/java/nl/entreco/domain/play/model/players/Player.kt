package nl.entreco.domain.play.model.players

/**
 * Created by Entreco on 18/11/2017.
 */
open class Player(val name: String, val id: Long = 0, val prefs: PlayerPrefs = PlayerPrefs(16)) {

    override fun toString(): String {
        return name
    }
}