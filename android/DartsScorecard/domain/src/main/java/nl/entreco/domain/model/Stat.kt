package nl.entreco.domain.model

/**
 * Created by entreco on 16/01/2018.
 */
data class Stat(val playerId: Long, val totalScore: Int, val nDarts: Int, val n180: Int, val n140: Int, val n100: Int, val nAtCheckout: Int, val nCheckouts : Int, val nBreaks : Int, val highest: List<Int>, val highestCo: List<Int>) {

    operator fun plus(stat: Stat?): Stat {
        stat?.let {
            val high = scoreList(stat)
            val highCo = checkoutList(stat)
            return copy(totalScore = totalScore + stat.totalScore,
                    nDarts = nDarts + stat.nDarts,
                    n180 = n180 + stat.n180,
                    n140 = n140 + stat.n140,
                    n100 = n100 + stat.n100,
                    nAtCheckout = nAtCheckout + stat.nAtCheckout,
                    nCheckouts = nCheckouts + stat.nCheckouts,
                    nBreaks = nBreaks + stat.nBreaks,
                    highest = high,
                    highestCo = highCo)
        }
        return this
    }

    private fun scoreList(stat: Stat): List<Int> {
        val high = ArrayList<Int>()
        high.addAll(highest)
        high.addAll(stat.highest)
        return high.sorted().reversed()
    }

    private fun checkoutList(stat: Stat): List<Int> {
        val high = ArrayList<Int>()
        high.addAll(highestCo)
        high.addAll(stat.highestCo)
        return high.sorted().reversed()
    }
}