package nl.entreco.dartsscorecard.play.stats

import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 13/01/2018.
 */
class CountStat(private val empty: String, private val countCondition: (Turn) -> Boolean) {
    private var count = 0
    override fun toString(): String {
        return when (count) {
            0 -> return empty
            else -> count.toString()
        }
    }

    operator fun plus(turn: Turn): CountStat {
        count += if (countCondition(turn)) 1 else 0
        return this
    }
}