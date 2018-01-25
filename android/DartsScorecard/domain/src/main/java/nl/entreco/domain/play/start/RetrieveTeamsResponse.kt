package nl.entreco.domain.play.start

import nl.entreco.domain.model.players.Team
import java.util.*

/**
 * Created by entreco on 06/01/2018.
 */
data class RetrieveTeamsResponse(val teams: Array<Team>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RetrieveTeamsResponse

        if (!Arrays.equals(teams, other.teams)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(teams)
    }
}