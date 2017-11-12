package nl.entreco.domain

class Arbiter(startScore: Score, private val numPlayers: Int) {

    private var scores = initForStart(startScore)

    private var legs = mutableListOf<Array<Score>>()

    fun handle(turn: Turn, currentPlayer: Int) : Int {
        applyScore(currentPlayer, turn)

        if (requiresNewLeg(currentPlayer)) return playerForNewLeg()

        return nextPlayer(currentPlayer)
    }

    private fun nextPlayer(currentPlayer: Int) = (currentPlayer + 1) % numPlayers

    private fun playerForNewLeg() = legs.size % numPlayers

    private fun requiresNewLeg(currentPlayer: Int): Boolean {
        if (legFinished(currentPlayer)) {
            legs.add(scores)
            initForNewLeg()
            return true
        }
        return false
    }

    private fun initForStart(score: Score) = Array(numPlayers, { score.copy() })

    private fun initForNewLeg() {
        scores.forEachIndexed { index, score -> scores[index] = score.inc() }
    }

    private fun applyScore(currentPlayer: Int, turn: Turn) {
        scores[currentPlayer] -= turn
    }

    private fun legFinished(currentPlayer: Int) = scores[currentPlayer].score <= 0


    fun getScores() : Array<Score> {
        return scores
    }

    override fun toString(): String {
        val builder = StringBuilder().append("${scores[0]}")
        scores.drop(1).forEach {  builder.append("\n$it") }
        return builder.toString()
    }
}