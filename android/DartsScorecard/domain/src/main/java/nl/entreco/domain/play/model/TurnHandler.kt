package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandler(val teams: Array<Team>, private val teamStartIndex : Int) {

    private var turns = -1
    private var legs = 0
    private var sets = 0

    private var currentPlayer: Player = NoPlayer()
        get() = if (teams.isEmpty() || turns < 0) NoPlayer() else team().next(legs, sets)

    private fun team() = teams[(teamStartIndex + turns + legs + sets) % teams.size]

    private fun teamIndex() = teams.indexOf(team())

    fun start(): Next {
        turns = 0
        legs = 0
        sets = 0
        return Next(State.START, team(),teamStartIndex, currentPlayer)
    }

    fun next(): Next {
        if (turns < 0) throw IllegalStateException("not started")
        turns++
        return Next(State.NORMAL, team(), teamIndex(), currentPlayer)
    }

    override fun toString(): String {
        return currentPlayer.toString()
    }

    fun nextLeg(): Next {
        turns = 0
        legs++
        return Next(State.LEG, team(), teamIndex(), currentPlayer)
    }

    fun nextSet(): Next {
        turns = 0
        legs = 0
        sets++
        return Next(State.SET, team(), teamIndex(), currentPlayer)
    }
}
