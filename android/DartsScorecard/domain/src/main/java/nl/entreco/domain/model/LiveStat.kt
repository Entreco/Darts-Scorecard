package nl.entreco.domain.model

/**
 * Created by entreco on 16/01/2018.
 */
data class LiveStat(val playerId: Long, val totalScore: Int, val nDarts: Int, val n180: Int, val n140: Int, val n100: Int, val n60: Int, val n20: Int,
                    val nAtCheckout: Int, val nCheckouts : Int, val nBreaks : Int, val highest: List<Int>, val highestCo: List<Int>,
                    val setTotals: Map<Int, Int>, val setDarts: Map<Int, Int>,
                    val setOuts: Map<Int, Int>, val setLegs: Map<Int, Int>
                    ) {

    operator fun plus(liveStat: LiveStat?): LiveStat {
        liveStat?.let {
            val high = scoreList(liveStat)
            val highCo = checkoutList(liveStat)
            val setTotals = setTotals(liveStat)
            val setDarts = setDarts(liveStat)
            val setOuts = setOuts(liveStat)
            val setLegs = setLegs(liveStat)
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
                    highestCo = highCo,
                    setTotals = setTotals,
                    setDarts = setDarts,
                    setOuts = setOuts,
                    setLegs = setLegs
                    )
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

    private fun setTotals(liveStat: LiveStat): Map<Int, Int> {
        val totals = HashMap<Int, Int>(setTotals)
        liveStat.setTotals.forEach { set, tot ->
            val current = totals.getOrDefault(set, setTotals[set] ?: 0)
            totals[set] = current + tot
        }
        return totals
    }

    private fun setDarts(liveStat: LiveStat): Map<Int, Int> {
        val darts = HashMap<Int, Int>(setDarts)
        liveStat.setDarts.forEach { set, tot ->
            val current = darts.getOrDefault(set, setDarts[set] ?: 0)
            darts[set] = current + tot
        }
        return darts
    }

    private fun setOuts(liveStat: LiveStat): Map<Int, Int> {
        val outs = HashMap<Int, Int>(setOuts)
        liveStat.setOuts.forEach { set, tot ->
            val current = outs.getOrDefault(set, setOuts[set] ?: 0)
            outs[set] = current + tot
        }
        return outs
    }

    private fun setLegs(liveStat: LiveStat): Map<Int, Int> {
        val legs = HashMap<Int, Int>(setLegs)
        liveStat.setLegs.forEach { set, tot ->
            val current = legs.getOrDefault(set, setLegs[set] ?: 0)
            legs[set] = current + tot
        }
        return legs
    }
}