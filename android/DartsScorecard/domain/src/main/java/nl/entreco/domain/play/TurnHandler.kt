package nl.entreco.domain.play

import nl.entreco.domain.model.Next
import nl.entreco.domain.model.Score
import nl.entreco.domain.model.players.NoPlayer
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.State
import nl.entreco.domain.model.players.Team

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandler(private val teamStartIndex: Int = 0, private var teams: Array<Team>) {

    private var turns = -1
    private var legs = 0
    private var sets = 0

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
        check()
        turns++
        val index = teamIndex()
        return Next(State.NORMAL, team(), index, currentPlayer, scores[index])
    }

    fun nextLeg(scores: Array<Score>): Next {
        check()
        turns = 0
        legs++
        val index = teamIndex()
        return Next(State.LEG, team(), index, currentPlayer, scores[index])
    }

    fun nextSet(scores: Array<Score>): Next {
        check()
        turns = 0
        legs = 0
        sets++
        val index = teamIndex()
        return Next(State.SET, team(), index, currentPlayer, scores[index])
    }

    private fun check() {
        if (teams.isEmpty()) throw IllegalStateException("cannot start without teams! turnHandler.teams = Array<Team>")
        if (turns < 0) throw IllegalStateException("not started")
    }

    fun indexOf(team: Team): Int {
        return teams.indexOf(team)
    }

    override fun toString(): String {
        return currentPlayer.toString()
    }
}
