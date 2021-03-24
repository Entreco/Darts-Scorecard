package nl.entreco.dartsscorecard.play.live

import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import nl.entreco.domain.model.LiveStat
import nl.entreco.domain.model.players.Team

/**
 * Created by entreco on 11/01/2018.
 */
class TeamLiveStatModel(
        val team: Team,
        private val liveStats: MutableList<LiveStat> = mutableListOf(),
) {

    companion object {
        const val empty = "--"
    }

    val name = ObservableField(team.toString())
    val avg = ObservableField(empty)
    val n180 = ObservableField(empty)
    val n140 = ObservableField(empty)
    val n100 = ObservableField(empty)
    val n60 = ObservableField(empty)
    val n20 = ObservableField(empty)
    val hScore = ObservableField(empty)
    val hCo = ObservableField(empty)
    val co = ObservableField(empty)
    val coRatio = ObservableField(empty)
    val breaks = ObservableField(empty)
    val image = ObservableField<String>(team.imageUrl())
    val breakdown = ObservableArrayMap<Int, TeamSetStat>()

    init {
        if (liveStats.isNotEmpty()) {
            update()
        }
    }

    fun append(updates: List<LiveStat>) {
        updates.forEach { liveStats.add(it) }
        if (liveStats.isNotEmpty()) {
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

        liveStats.forEach { stat ->
            stat.setDarts.forEach {
                updateSetAverage(it.key)
            }
        }
    }

    private fun updateBreaksMade() {
        val value = liveStats.sumBy { it.nBreaks }
        breaks.set("$value")
    }

    private fun updateDoublePercentage() {
        val aggregator = liveStats.sumBy { it.nCheckouts }
        val denominator = liveStats.sumBy { it.nAtCheckout }
        co.set("%d/%d".format(aggregator, denominator))

        when (denominator) {
            0 -> coRatio.set(empty)
            else -> coRatio.set("%.2f".format(100 * aggregator / denominator.toDouble()))
        }
    }

    private fun updateHighestScore() {
        when (val value = liveStats
                .filter { it.highest.isNotEmpty() }
                .maxByOrNull { it.highest[0] }
                ?.highest?.firstOrNull()) {
            null -> hScore.set(empty)
            else -> hScore.set("$value")
        }
    }

    private fun updateHighestCheckout() {
        when (val value = liveStats
                .filter { it.highestCo.isNotEmpty() }
                .maxByOrNull { it.highestCo[0] }
                ?.highestCo?.firstOrNull()) {
            null -> hCo.set(empty)
            else -> hCo.set("$value")
        }
    }

    private fun update20s() {
        val value = liveStats.sumBy { it.n20 }
        n20.set("$value")
    }

    private fun update60s() {
        val value = liveStats.sumBy { it.n60 }
        n60.set("$value")
    }

    private fun update100s() {
        val value = liveStats.sumBy { it.n100 }
        n100.set("$value")
    }

    private fun update140s() {
        val value = liveStats.sumBy { it.n140 }
        n140.set("$value")
    }

    private fun update180s() {
        val value = liveStats.sumBy { it.n180 }
        n180.set("$value")
    }

    private fun updateAverage() {
        val aggregator = liveStats.sumBy { it.totalScore }
        val denominator = liveStats.sumBy { it.nDarts }

        when (denominator) {
            0 -> avg.set(empty)
            else -> avg.set("%.2f".format(aggregator / denominator.toDouble() * 3))
        }
    }

    private fun updateSetAverage(setIndex: Int) {
        val total = liveStats.sumBy { it.setTotals.getOrDefault(setIndex, 0) }
        val darts = liveStats.sumBy { it.setDarts.getOrDefault(setIndex, 0) }
        val outs = liveStats.sumBy { it.setOuts.getOrDefault(setIndex, 0) }
        val legs = liveStats.sumBy { it.setLegs.getOrDefault(setIndex, 0) }
        update(setIndex, darts, total, outs, legs)
    }

    private fun update(set: Int, darts: Int, total: Int, nAtCheckout: Int, checkouts: Int) {
        val avg = when (darts) {
            0 -> empty
            else -> "%.2f".format(total / darts.toDouble() * 3)
        }
        val co = "$checkouts/$nAtCheckout"
        val du = when (nAtCheckout) {
            0 -> empty
            else -> "%.2f".format(100 * checkouts / nAtCheckout.toDouble())
        }
        breakdown[set] = TeamSetStat(set, avg, co, du)
    }
}
