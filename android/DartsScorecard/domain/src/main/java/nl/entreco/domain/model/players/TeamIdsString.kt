package nl.entreco.domain.model.players

/**
 * Created by Entreco on 17/12/2017.
 */
data class TeamIdsString(private val teamString: String) {
    override fun toString(): String {
        return teamString
    }
}