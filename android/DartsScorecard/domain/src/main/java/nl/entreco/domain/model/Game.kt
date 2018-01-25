package nl.entreco.domain.model

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.Arbiter

data class Game(val id: Long = 0, val arbiter: Arbiter) {

    lateinit var next: Next
    private lateinit var previousState: State
    private val starters = emptyList<Team>().toMutableList()
    private var newMatchSetOrLeg: Boolean = false

    var scores = emptyArray<Score>()
        get() = arbiter.getScores()

    fun start(startIndex: Int, teams: Array<Team>): Game {
        next = arbiter.start(startIndex, teams)
        starters.clear()
        starters.add( 0, next.team)
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
        newMatchSetOrLeg = when (next.state) {
            State.MATCH, State.LEG, State.SET -> {
                starters.add( 0, next.team)
                true
            }
            else -> {
                false
            }
        }
    }

    fun previousScore(): Score {
        return arbiter.getPreviousScore()
    }

    fun isNewMatchLegOrSet(): Boolean {
        return newMatchSetOrLeg
    }

    fun getTurnCount(): Int {
        return arbiter.getTurnCount()
    }

    fun wasBreakMade(by: Player): Boolean {
        if(starters.size < 2) return false
        return newMatchSetOrLeg && !starters[1].contains(by)
    }
}