package nl.entreco.domain.model

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.Arbiter

data class Game(val id: Long = 0, val arbiter: Arbiter) {

    lateinit var next: Next
    private lateinit var previousState: State
    private lateinit var startTeam: Team
    private var newMatchSetOrLeg: Boolean = false

    var scores = emptyArray<Score>()
        get() = arbiter.getScores()

    fun start(startIndex: Int, teams: Array<Team>): Game {
        next = arbiter.start(startIndex, teams)
        startTeam = next.team
        previousState = next.state
        newMatchSetOrLeg = true
        return this
    }

    fun handle(turn: Turn) {
        previousState = next.state
        if (State.MATCH != next.state) {
            next = arbiter.handle(turn, next)
            updateStartTeam(next)
        }
    }

    private fun updateStartTeam(next: Next) {
        when (next.state) {
            State.MATCH, State.LEG, State.SET -> {
                startTeam = next.team
                newMatchSetOrLeg = true
            }
            else -> {
                newMatchSetOrLeg = false
            }
        }
    }

    fun previousScore(): Score {
        return arbiter.getPreviousScore()
    }

    fun isNewMatchlegOrSet(): Boolean {
        return newMatchSetOrLeg
    }

    fun getTurnCount(): Int {
        return arbiter.getTurnCount()
    }

    fun wasBreakMade(by: Player): Boolean {
        return previousScore().score == 0 && startTeam.contains(by)
    }
}