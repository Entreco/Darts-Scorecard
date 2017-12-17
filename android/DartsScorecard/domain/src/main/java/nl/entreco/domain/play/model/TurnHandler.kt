package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.NoPlayer
import nl.entreco.domain.play.model.players.Player
import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandler(private val teamStartIndex: Int = 0) {

    private var turns = -1
    private var legs = 0
    private var sets = 0

    var teams: Array<Team> = emptyArray()

    private var currentPlayer: Player = NoPlayer()
        get() = if (teams.isEmpty() || turns < 0) NoPlayer() else team().next(legs, sets)

    private fun team() = teams[(teamStartIndex + turns + legs + sets) % teams.size]

    private fun teamIndex() = teams.indexOf(team())

    fun start(required: Score = Score()): Next {
        if (teams.isEmpty()) throw IllegalStateException("cannot start without teams! turnHandler.teams = Array<Team>")
        turns = 0
        legs = 0
        sets = 0
        return Next(State.START, team(), teamStartIndex, currentPlayer, required)
    }

    fun next(scores: Array<Score>): Next {
        if (teams.isEmpty()) throw IllegalStateException("cannot start without teams! turnHandler.teams = Array<Team>")
        if (turns < 0) throw IllegalStateException("not started")
        turns++
        val index = teamIndex()
        return Next(State.NORMAL, team(), index, currentPlayer, scores[index])
    }

    override fun toString(): String {
        return currentPlayer.toString()
    }

    fun nextLeg(scores: Array<Score>): Next {
        if (teams.isEmpty()) throw IllegalStateException("cannot start without teams! turnHandler.teams = Array<Team>")
        turns = 0
        legs++
        val index = teamIndex()
        return Next(State.LEG, team(), index, currentPlayer, scores[index])
    }

    fun nextSet(scores: Array<Score>): Next {
        if (teams.isEmpty()) throw IllegalStateException("cannot start without teams! turnHandler.teams = Array<Team>")
        turns = 0
        legs = 0
        sets++
        val index = teamIndex()
        return Next(State.SET, team(), index, currentPlayer, scores[index])
    }
}
