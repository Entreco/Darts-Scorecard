package nl.entreco.domain.play.start

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.settings.ScoreSettings

/**
 * Created by entreco on 06/01/2018.
 */
data class Play01Response(val game: Game, val settings: ScoreSettings, val teams: Array<Team>, val teamIds: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Play01Response

        if (game != other.game) return false
        if (settings != other.settings) return false
        if (!teams.contentEquals(other.teams)) return false
        if (teamIds != other.teamIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = game.hashCode()
        result = 31 * result + settings.hashCode()
        result = 31 * result + teams.contentHashCode()
        result = 31 * result + teamIds.hashCode()
        return result
    }
}