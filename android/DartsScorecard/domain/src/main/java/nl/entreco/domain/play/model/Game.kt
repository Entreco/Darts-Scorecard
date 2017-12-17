package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.State
import nl.entreco.domain.play.model.players.Team

data class Game(val id: Long = 0, val arbiter: Arbiter) {

    lateinit var next: Next
    var state = "game on"
    var scores = arbiter.getScores()
        get() = arbiter.getScores()

    fun start(): Game {
        next = arbiter.start()
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

    fun teams(): Array<Team> {
        return arbiter.teams()
    }

    private val GAME_FINISHED = "game finished"
}