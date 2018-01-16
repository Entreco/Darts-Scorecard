package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableField
import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 11/01/2018.
 */
class TeamStatModel(val name: String, turns: Array<Turn> = emptyArray()) {

    val avg = ObservableField<AvgStat>(AvgStat(empty, 2, { it.total() }, { it.dartsUsed() }))
    val n180 = ObservableField<CountStat>(CountStat(empty, { it.total() == 180 }))
    val n140 = ObservableField<CountStat>(CountStat(empty, { (it.total() in 140..179) }))
    val n100 = ObservableField<CountStat>(CountStat(empty, { (it.total() in 100..139) }))
    val highest = ObservableField<RecordStat>(RecordStat(empty, { old, new -> (old >= new) }))

    init {
        turns.forEach { turn ->
            applyStats(turn)
        }
    }

    fun applyStats(turn: Turn): TeamStatModel {
        avg.set(avg.get() + turn)
        n180.set(n180.get() + turn)
        n140.set(n140.get() + turn)
        n100.set(n100.get() + turn)
        return this
    }

    companion object {
        const val empty = "-"
    }
}