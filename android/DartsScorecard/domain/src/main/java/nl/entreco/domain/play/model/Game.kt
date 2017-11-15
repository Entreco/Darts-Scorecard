package nl.entreco.domain.play.model

data class Game(val arbiter: Arbiter) {

    private var currentPlayer = 0

    var state = "game on"
    var scores = arbiter.getScores()
        get() = arbiter.getScores()

    fun start() {
        state = "player 1 to throw first"
    }

    fun handle(turn: Turn) {
        if (GAME_FINISHED != state) {
            currentPlayer = arbiter.handle(turn, currentPlayer)
            state = if (currentPlayer < 0) GAME_FINISHED
            else "player ${currentPlayer + 1} to throw"
        }
    }

    private val GAME_FINISHED = "game finished"

}