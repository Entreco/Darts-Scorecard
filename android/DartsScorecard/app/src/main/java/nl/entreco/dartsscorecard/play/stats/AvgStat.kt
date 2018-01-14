package nl.entreco.dartsscorecard.play.stats

import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 13/01/2018.
 */
class AvgStat(private val empty: String, private val precision: Int, private val enumerator: (Turn) -> Int, private val denominator: (Turn) -> Int) {

    private var count: Int = 0
    private var num: Int = 0

    override fun toString(): String {
        return when (num) {
            0 -> return empty
            else -> String.format("%.${precision}f", (count / num) * 3.0f)
        }
    }

    operator fun plus(turn: Turn): AvgStat {
        count += enumerator(turn)
        num += denominator(turn)
        return this
    }
}