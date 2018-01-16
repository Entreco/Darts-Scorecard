package nl.entreco.domain.model

import nl.entreco.domain.model.players.Team
import nl.entreco.domain.play.Arbiter

data class Game(val id: Long = 0, val arbiter: Arbiter) {

    lateinit var next: Next
    var scores = emptyArray<Score>()
        get() = arbiter.getScores()

    fun start(startIndex: Int, teams: Array<Team>): Game {
        next = arbiter.start(startIndex, teams)
        return this
    }

    fun handle(turn: Turn) {
        if (State.MATCH != next.state) {
            next = arbiter.handle(turn, next)
        }
    }

    fun previousScore(): Score {
        return arbiter.getPreviousScore()
    }
}