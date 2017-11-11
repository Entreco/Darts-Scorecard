package nl.entreco.domain

class Arbiter(private val startScore: Score, private val numPlayers: Int) {

    private var scores = initWithStart(startScore)

    private var legs = mutableListOf<Array<Score>>()

    private fun initWithStart(score: Score) = Array(numPlayers, { score.copy() })

    fun handle(turn: Turn, currentPlayer: Int) : Int {
        scores[currentPlayer] -= turn

        if(legFinished(currentPlayer)){
            legs.add(scores)
            scores = initWithStart(startScore.inc())
            return legs.size % numPlayers
        }

        return (currentPlayer + 1) % numPlayers
    }

    private fun legFinished(currentPlayer: Int) = scores[currentPlayer].score == 0

    fun printScore() : String {
        val builder = StringBuilder().append("${scores[0]}")

        scores.drop(1).forEach {  builder.append(", $it") }
        builder.append(" (leg:${legs.size})")
        return builder.toString()
    }
}