package nl.entreco.domain.play.model

import nl.entreco.domain.play.model.players.State

data class Game(val arbiter: Arbiter) {

    lateinit var next: Next
    var state = "game on"
    var scores = arbiter.getScores()
        get() = arbiter.getScores()

    fun start() {
        next = arbiter.start()
        state = "${next.player} to throw first"
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