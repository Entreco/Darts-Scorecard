package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 18/11/2017.
 */
class WhosNext(vararg val teams: Team = emptyArray()) {

    private var turns = -1
    private var legs = 0
    private var sets = 0

    private var currentPlayer: Player = NoPlayer()
        get() = if (teams.isEmpty() || turns < 0) NoPlayer() else team().next(legs, sets)

    private fun team() = teams[(turns + legs + sets) % teams.size]

    fun start(): Player {
        turns = 0
        legs = 0
        sets = 0
        return currentPlayer
    }

    fun next(): Player {
        if (turns < 0) throw IllegalStateException("not started")
        turns++
        return currentPlayer
    }

    override fun toString(): String {
        return currentPlayer.toString()
    }

    fun nextLeg(): Player {
        turns = 0
        legs++
        return currentPlayer
    }

    fun nextSet(): Player {
        turns = 0
        legs = 0
        sets++
        return currentPlayer
    }

}
