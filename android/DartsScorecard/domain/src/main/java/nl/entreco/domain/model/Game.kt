package nl.entreco.domain.model

import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.Arbiter
import kotlin.math.max

data class Game(val id: Long = 0, val arbiter: Arbiter) {

    lateinit var next: Next
    private lateinit var previousState: State
    private val starters = emptyList<Team>().toMutableList()
    private var newMatchSetOrLeg: Boolean = false

    val scores
        get() = arbiter.getScores()

    fun start(startIndex: Int, teams: Array<Team>): Game {
        next = arbiter.start(startIndex, teams)
        starters.clear()
        starters.add(0, next.team)
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
        if (previousState == State.START) return
        newMatchSetOrLeg = when (next.state) {
            State.MATCH, State.LEG, State.SET -> {
                starters.add(0, next.team)
                true
            }
            else                              -> {
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

    fun getSetCount(): Int {
        val score = arbiter.getScores().sumBy { it.set }
        val isNew: Boolean = arbiter.getScores().count { it.score == it.settings.startScore && it.leg == 0 } == arbiter.getScores().size
        return max(0, score - if (isNew) 1 else 0)
    }

    fun getLegCount(): Int {
        return arbiter.getScores().sumBy { it.set * 100 + it.leg }
    }

    fun wasBreakMade(by: Player): Boolean {
        if (starters.size < 2) return false
        return newMatchSetOrLeg && !starters[1].contains(by)
    }
}