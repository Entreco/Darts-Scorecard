package nl.entreco.domain.play.model

import nl.entreco.domain.settings.ScoreSettings

data class Score(var score: Int = 501, val leg: Int = 0, val set: Int = 0, private val settings: ScoreSettings = ScoreSettings()) {

    operator fun minusAssign(turn: Turn) {
        this.score -= turn.d1.value() + turn.d2.value() + turn.d3.value()
    }

    fun rollLeg(): Score {
        val legs = if (score <= 0 && leg + 1 <= settings.numLegs) (leg + 1) % settings.numLegs else leg
        val sets = if (score <= 0 && legs == 0 && set + 1 <= settings.numSets) set + 1 else set
        return Score(settings.startScore, legs, sets, settings)
    }

    fun rollSet(): Score {
        val legs = 0
        val sets = if (score <= 0) set + 1 else set
        return Score(settings.startScore, legs, sets, settings)
    }

    override fun toString(): String {
        return "$score | $leg | $set"
    }

    fun matchFinished(): Boolean = setFinished() && set >= settings.numSets - 1
    fun setFinished(): Boolean = legFinished() && leg >= settings.numLegs - 1
    fun legFinished(): Boolean = score <= 0
}