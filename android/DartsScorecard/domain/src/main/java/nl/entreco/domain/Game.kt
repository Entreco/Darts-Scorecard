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
        currentPlayer = arbiter.handle(turn, currentPlayer)
        state = "player ${currentPlayer + 1} to throw"
    }

}