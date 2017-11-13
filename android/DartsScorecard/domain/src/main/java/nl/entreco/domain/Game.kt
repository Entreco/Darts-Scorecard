package nl.entreco.domain

class Game(startScore: Int = 501, numPlayers: Int = 2) {

    private var currentPlayer = 0

    val arbiter = Arbiter(Score(startScore,0,0,startScore,3,1), numPlayers)
    var state = "game on"
    var scores  = arbiter.getScores()
        get() = arbiter.getScores()

    fun start() {
        state = "player 1 to throw first"
    }

    fun handle(turn: Turn) {
        if(GAME_FINISHED != state) {
            currentPlayer = arbiter.handle(turn, currentPlayer)
            if (currentPlayer < 0) state = GAME_FINISHED
            else state = "player ${currentPlayer + 1} to throw"
        }
    }

    private val GAME_FINISHED = "game finished"

}