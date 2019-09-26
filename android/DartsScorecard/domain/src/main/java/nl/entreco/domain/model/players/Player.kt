package nl.entreco.domain.model.players

/**
 * Created by Entreco on 18/11/2017.
 */
open class Player(val name: String, val id: Long = 0, val prefs: PlayerPrefs = PlayerPrefs(16), val image: String? = null) {

    open val isHuman: Boolean = true

    override fun toString(): String {
        return name
    }
}
