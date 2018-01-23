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
        val value = stats.sumBy { it.nBreaks }
        breaks.set("$value")
    }

    private fun updateDoublePercentage() {
        val aggregator = stats.sumBy { it.nCheckouts }
        val denominator = stats.sumBy { it.nAtCheckout }

        when (denominator) {
            0 -> co.set(empty)
            else -> co.set("%.2f".format(aggregator / denominator.toDouble() * 100) + "%")
        }
    }

    private fun updateHighestCheckout() {
        val value = stats
                .filter { it.highestCo.isNotEmpty() }
                .maxBy { it.highestCo[0] }
                ?.highestCo?.firstOrNull()
        when (value) {
            null -> hCo.set(empty)
            else -> hCo.set("$value")
        }
    }

    private fun update100s() {
        val value = stats.sumBy { it.n100 }
        n100.set("$value")
    }

    private fun update140s() {
        val value = stats.sumBy { it.n140 }
        n140.set("$value")
    }

    private fun update180s() {
        val value = stats.sumBy { it.n180 }
        n180.set("$value")
    }

    private fun updateAverage() {
        val aggregator = stats.sumBy { it.totalScore }
        val denominator = stats.sumBy { it.nDarts }

        when (denominator) {
            0 -> avg.set(empty)
            else -> avg.set("%.2f".format(aggregator / denominator.toDouble() * 3))
        }
    }
}