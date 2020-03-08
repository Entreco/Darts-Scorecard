package nl.entreco.domain.play.start

import nl.entreco.domain.model.players.Team

/**
 * Created by entreco on 06/01/2018.
 */
class RetrieveTeamsResponse(val teams: Array<Team>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RetrieveTeamsResponse

        if (!teams.contentEquals(other.teams)) return false

        return true
    }

    override fun hashCode(): Int {
        return teams.contentHashCode()
    }
}