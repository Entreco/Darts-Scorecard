package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team

data class Game(val id: Long = 0, val arbiter: Arbiter) {

    lateinit var next: Next
    var state = "game on"
    var scores = emptyArray<Score>()
        get() = arbiter.getScores()

    fun start(startIndex: Int, teams: Array<Team>): Game {
        next = arbiter.start(startIndex, teams)
        state = "${next.player} to throw first"
        return this
    }

    fun handle(turn: Turn) {
        if (GAME_FINISHED != state) {
            next = arbiter.handle(turn, next)
            state = if (next.state == State.MATCH) GAME_FINISHED
            else "player ${next.player} to throw"
        }
    }

    private val GAME_FINISHED = "game finished"
}