package nl.entreco.domain.play.model.players

/**
 * Created by Entreco on 18/11/2017.
 */
class Team(val players: List<Player> = emptyList()){
    private var current = 0
    var currentPlayer : Player = NoPlayer()
        get() = if(players.isEmpty()) NoPlayer() else players[current % players.size]

    fun next(): Player {
        current++
        return currentPlayer
    }

    override fun toString(): String {
        return StringBuilder(firstOrNo().name).apply {
            players.drop(1).forEach { append(" & ").append(it.name) }
        }.toString()
    }

    private fun firstOrNo(): Player {
        if(players.isEmpty()) return NoPlayer()
        else return players[current]
    }
}