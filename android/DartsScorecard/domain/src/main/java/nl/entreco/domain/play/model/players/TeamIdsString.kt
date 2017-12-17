package nl.entreco.domain.play.model.players

/**
 * Created by Entreco on 17/12/2017.
 */
data class TeamIdsString(private val teamString: String) {
    override fun toString(): String {
        return teamString
    }
}