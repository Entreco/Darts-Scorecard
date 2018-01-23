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
        updateAverage()
        update180s()
        update140s()
        update100s()
        updateHighestCheckout()
        updateDoublePercentage()
        updateBreaksMade()
    }

    private fun updateBreaksMade() {
        val g = stats.sumBy { it.nBreaks }
        breaks.set("$g")
    }

    private fun updateDoublePercentage() {
        val f1 = stats.sumBy { it.nCheckouts }
        val f2 = stats.sumBy { it.nAtCheckout }
        co.set("%.2f".format(f1 / f2.toDouble()) + "%")
    }

    private fun updateHighestCheckout() {
        val e = stats.maxBy { it.highestCo[0] }?.highestCo?.first()
        hCo.set("$e")
    }

    private fun update100s() {
        val d = stats.sumBy { it.n100 }
        n100.set("$d")
    }

    private fun update140s() {
        val c = stats.sumBy { it.n140 }
        n140.set("$c")
    }

    private fun update180s() {
        val b = stats.sumBy { it.n180 }
        n180.set("$b")
    }

    private fun updateAverage() {
        val a1 = stats.sumBy { it.totalScore }
        val a2 = stats.sumBy { it.nDarts }
        avg.set("%.2f".format(a1 / a2.toDouble() * 3))
    }
}