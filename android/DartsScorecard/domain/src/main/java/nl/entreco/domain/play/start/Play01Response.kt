package nl.entreco.domain.play.start

import nl.entreco.domain.model.Game
import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.players.Team
import java.util.*

/**
 * Created by entreco on 06/01/2018.
 */
data class Play01Response(val game: Game, val teams: Array<Team>, val turns: List<Pair<Long, Turn>>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Play01Response

        if (game != other.game) return false
        if (!Arrays.equals(teams, other.teams)) return false
        if (turns != other.turns) return false

        return true
    }

    override fun hashCode(): Int {
        var result = game.hashCode()
        result = 31 * result + Arrays.hashCode(teams)
        result = 31 * result + turns.hashCode()
        return result
    }

}