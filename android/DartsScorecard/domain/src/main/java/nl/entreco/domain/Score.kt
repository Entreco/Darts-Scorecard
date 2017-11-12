package nl.entreco.domain

data class Score(var score: Int = 501, val leg: Int = 0, val set: Int = 0, private val startScore: Int = 501, private val numLegs: Int = 3, private val numSets: Int = 1) {

    operator fun minusAssign(turn: Turn) {
        this.score -= turn.d1 + turn.d2 + turn.d3
    }

    fun rollLeg(): Score {
        val legs = if (score <= 0 && leg + 1 <= numLegs) (leg + 1) % numLegs else leg
        val sets = if (score <= 0 && legs == 0 && set + 1 <= numSets) set + 1 else set
        return Score(startScore, legs, sets)
    }

    fun rollSet(): Score {
        val legs = 0
        val sets = if (score <= 0 && set + 1 <= numSets) set + 1 else set
        return Score(startScore, legs, sets)
    }

    override fun toString(): String {
        return "$score | $leg | $set"
    }

    fun setFinished(): Boolean = score <= 0 && leg >= numLegs - 1
    fun legFinished(): Boolean = score <= 0
}