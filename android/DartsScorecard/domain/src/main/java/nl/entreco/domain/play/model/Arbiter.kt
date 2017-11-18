package nl.entreco.domain.play.model

class Arbiter(private val initial: Score, private val numPlayers: Int) {

    private var scores = initForStart(initial)

    private var legs = mutableListOf<Array<Score>>()

    private var sets = mutableListOf<MutableList<Array<Score>>>()

    fun handle(turn: Turn, currentPlayer: Int): Int {
        applyScore(currentPlayer, turn)

        if (gameShotAndTheMatch(currentPlayer)) return -1
        if (requiresNewSet(currentPlayer)) return playerForNewSet()
        else if (requiresNewLeg(currentPlayer)) return playerForNewLeg()

        return nextPlayer(currentPlayer)
    }

    private fun nextPlayer(currentPlayer: Int) = (currentPlayer + 1) % numPlayers

    private fun playerForNewLeg() = legs.size % numPlayers
    private fun playerForNewSet() = sets.size % numPlayers

    private fun gameShotAndTheMatch(currentPlayer: Int): Boolean {
        if (matchFinished(currentPlayer)) {
            legs.add(scores)
            sets.add(legs)
            initForNewMatch()
            return true
        }
        return false
    }

    private fun requiresNewLeg(currentPlayer: Int): Boolean {
        if (legFinished(currentPlayer)) {
            legs.add(scores)
            initForNewLeg()
            return true
        }
        return false
    }

    private fun requiresNewSet(currentPlayer: Int): Boolean {
        if (setFinished(currentPlayer)) {
            legs.add(scores)
            sets.add(legs)
            initForNewSet()
            return true
        }
        return false
    }

    private fun initForStart(score: Score) = Array(numPlayers, { score.copy() })

    private fun initForNewLeg() {
        scores.forEachIndexed { index, score -> scores[index] = score.rollLeg() }
    }

    private fun initForNewSet() {
        scores.forEachIndexed { index, score -> scores[index] = score.rollSet() }
    }

    private fun initForNewMatch() {
        initForNewSet()
    }

    private fun applyScore(currentPlayer: Int, turn: Turn) {
        scores[currentPlayer] -= turn
    }

    private fun legFinished(currentPlayer: Int) = scores[currentPlayer].legFinished()
    private fun setFinished(currentPlayer: Int) = scores[currentPlayer].setFinished()
    private fun matchFinished(currentPlayer: Int) = scores[currentPlayer].matchFinished()

    fun getScores(): Array<Score> {
        return scores
    }

    fun getLegs(): MutableList<Array<Score>> {
        return legs
    }

    fun getSets(): MutableList<MutableList<Array<Score>>> {
        return sets
    }

    override fun toString(): String {
        val builder = StringBuilder().append("${scores[0]}")
        scores.drop(1).forEach { builder.append("\n$it") }
        return builder.toString()
    }

    fun numberOfSets(): Int {
        return initial.leg
    }
}