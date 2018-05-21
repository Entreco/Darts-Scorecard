package nl.entreco.domain.model

/**
 * Created by entreco on 16/01/2018.
 */
data class LiveStat(val playerId: Long, val totalScore: Int, val nDarts: Int, val n180: Int, val n140: Int, val n100: Int, val n60: Int, val n20: Int,
                    val nAtCheckout: Int, val nCheckouts : Int, val nBreaks : Int, val highest: List<Int>, val highestCo: List<Int>) {

    operator fun plus(liveStat: LiveStat?): LiveStat {
        liveStat?.let {
            val high = scoreList(liveStat)
            val highCo = checkoutList(liveStat)
            return copy(totalScore = totalScore + liveStat.totalScore,
                    nDarts = nDarts + liveStat.nDarts,
                    n180 = n180 + liveStat.n180,
                    n140 = n140 + liveStat.n140,
                    n100 = n100 + liveStat.n100,
                    n60 = n60 + liveStat.n60,
                    n20 = n20 + liveStat.n20,
                    nAtCheckout = nAtCheckout + liveStat.nAtCheckout,
                    nCheckouts = nCheckouts + liveStat.nCheckouts,
                    nBreaks = nBreaks + liveStat.nBreaks,
                    highest = high,
                    highestCo = highCo)
        }
        return this
    }

    private fun scoreList(liveStat: LiveStat): List<Int> {
        val high = ArrayList<Int>()
        high.addAll(highest)
        high.addAll(liveStat.highest)
        return high.sorted().reversed()
    }

    private fun checkoutList(liveStat: LiveStat): List<Int> {
        val high = ArrayList<Int>()
        high.addAll(highestCo)
        high.addAll(liveStat.highestCo)
        return high.sorted().reversed()
    }
}