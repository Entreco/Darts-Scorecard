package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableField
import nl.entreco.domain.model.players.Team

/**
 * Created by entreco on 11/01/2018.
 */
class TeamStatModel(team: Team) {

    val name = ObservableField<String>(team.toString())
    val avg = ObservableField<AvgStat>(AvgStat(empty, 2, { it.total() }, { it.dartsUsed() }))
    val n180 = ObservableField<CountStat>(CountStat(empty, { it.total() == 180 }))
    val n140 = ObservableField<CountStat>(CountStat(empty, { (it.total() in 140..179) }))
    val n100 = ObservableField<CountStat>(CountStat(empty, { (it.total() in 100..139) }))
    val highest = ObservableField<RecordStat>(RecordStat(empty, { old, new -> (old >= new) }))

    init {

    }

    companion object {
        const val empty = "-"
    }
}