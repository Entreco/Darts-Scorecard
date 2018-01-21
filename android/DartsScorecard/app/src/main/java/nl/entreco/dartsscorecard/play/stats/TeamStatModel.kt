package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableField
import nl.entreco.domain.model.Stat
import nl.entreco.domain.model.players.Team

/**
 * Created by entreco on 11/01/2018.
 */
class TeamStatModel(team: Team, private val stats: MutableList<Stat> = mutableListOf()) {

    companion object {
        const val empty = "-"
    }

    val name = ObservableField<String>(team.toString())
    val avg = ObservableField<String>(empty)
    val n180 = ObservableField<String>(empty)
    val n140 = ObservableField<String>(empty)
    val n100 = ObservableField<String>(empty)
    val hCo = ObservableField<String>(empty)
    val co = ObservableField<String>(empty)
    val breaks = ObservableField<String>(empty)

    init {
        if (stats.isNotEmpty()) {
            update()
        }
    }

    fun append(updates: List<Stat>) {
        updates.forEach { stats.add(it) }
        update()
    }

    private fun update() {
        val a1 = stats.sumBy { it.totalScore }
        val a2 = stats.sumBy { it.nDarts }
        avg.set("%.2f".format(a1/a2.toDouble() * 3))
        val b = stats.sumBy { it.n180 }
        n180.set("$b")
        val c = stats.sumBy { it.n140 }
        n140.set("$c")
        val d = stats.sumBy { it.n100 }
        n100.set("$d")
        val e = stats.maxBy { it.highestCo[0] }?.highestCo?.first()
        hCo.set("$e")
        val f1 = stats.sumBy { it.nCheckouts }
        val f2 = stats.sumBy { it.nAtCheckout }
        co.set("%.2f".format(f1/f2.toDouble()) + "%")
        val g = stats.sumBy { it.nBreaks }
        breaks.set("$g")
    }
}