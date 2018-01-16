package nl.entreco.dartsscorecard.play.stats

import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 16/01/2018.
 */
class RecordStat(private val empty: String, private val recordCheck: (Int, Int) -> Boolean) {
    private var record = Int.MIN_VALUE

    override fun toString(): String {
        return when (record) {
            Int.MIN_VALUE -> return empty
            else -> record.toString()
        }
    }

    operator fun plus(turn: Turn): RecordStat {
        record = if (recordCheck(turn.total(), record)) turn.total() else 0
        return this
    }
}