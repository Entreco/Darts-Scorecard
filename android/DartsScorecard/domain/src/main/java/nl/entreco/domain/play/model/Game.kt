package nl.entreco.domain.play.model

data class Game(val arbiter: Arbiter) {

    var next: Next? = null
    var state = "game on"
    var scores = arbiter.getScores()
        get() = arbiter.getScores()

    fun start() {
        state = "player 1 to throw first"
    }

    fun handle(turn: Turn) {
        if (GAME_FINISHED != state) {
            next = arbiter.handle(turn, next)
            state = if (next == null) GAME_FINISHED
            else "player ${next?.player} to throw"
        }
    }

    private val GAME_FINISHED = "game finished"
}