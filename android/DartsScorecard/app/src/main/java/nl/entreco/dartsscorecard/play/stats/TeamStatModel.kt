package nl.entreco.dartsscorecard.play.stats

import android.databinding.ObservableField
import nl.entreco.domain.model.Stat
import nl.entreco.domain.model.players.Team

/**
 * Created by entreco on 11/01/2018.
 */
class TeamStatModel(val team: Team, private val stats: MutableList<Stat> = mutableListOf()) {

    companion object {
        const val empty = "--"
    }

    val name = ObservableField<String>(team.toString())
    val avg = ObservableField<String>(empty)
    val n180 = ObservableField<String>(empty)
    val n140 = ObservableField<String>(empty)
    val n100 = ObservableField<String>(empty)
    val n60 = ObservableField<String>(empty)
    val n20 = ObservableField<String>(empty)
    val hScore = ObservableField<String>(empty)
    val hCo = ObservableField<String>(empty)
    val co = ObservableField<String>(empty)
    val breaks = ObservableField<String>(empty)
    val image = ObservableField<String>(team.imageUrl())

    init {
        if (stats.isNotEmpty()) {
            update()
        }
    }

    fun append(updates: List<Stat>) {
        updates.forEach { stats.add(it) }
        if (stats.isNotEmpty()) {
            update()
        }
    }

    private fun update() {
        updateAverage()
        update180s()
        update140s()
        update100s()
        update60s()
        update20s()
        updateHighestScore()
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

    private fun updateHighestScore() {
        val value = stats
                .filter { it.highest.isNotEmpty() }
                .maxBy { it.highest[0] }
                ?.highest?.firstOrNull()
        when (value) {
            null -> hScore.set(empty)
            else -> hScore.set("$value")
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

    private fun update20s() {
        val value = stats.sumBy { it.n20 }
        n20.set("$value")
    }
    private fun update60s() {
        val value = stats.sumBy { it.n60 }
        n60.set("$value")
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
