package nl.entreco.domain.model

data class Turn (internal val d1: Dart = Dart.NONE, internal val d2: Dart = Dart.NONE, internal val d3: Dart = Dart.NONE){
    private val sum = d1.points() + d2.points() + d3.points()

    override fun toString(): String {
        return "${d1.desc()} ${d2.desc()} ${d3.desc()} ($sum)"
    }

    operator fun plus(other: Dart) : Turn {
        return when {
            other == Dart.NONE -> throw IllegalStateException("Darts.NONE is reserved for initialization -> use Darts.ZERO for 'no score'")
            d1 == Dart.NONE -> Turn(other)
            d2 == Dart.NONE -> Turn(d1, other)
            d3 == Dart.NONE -> Turn(d1, d2, other)
            else -> throw IllegalStateException("Already 3 darts thrown, $d1, $d2, $d3")
        }
    }

    fun first(): Dart{
        return d1
    }

    fun second(): Dart{
        return d2
    }

    fun third(): Dart{
        return d3
    }

    fun last(): Dart {
        return when {
            d1 == Dart.NONE -> Dart.NONE
            d2 == Dart.NONE -> d1
            d3 == Dart.NONE -> d2
            else -> d3
        }
    }

    fun dartsUsed() : Int {
        return 3 - dartsLeft()
    }

    fun dartsLeft() : Int {
        return when {
            d1 == Dart.NONE -> 3
            d2 == Dart.NONE -> 2
            d3 == Dart.NONE -> 1
            else -> 0
        }
    }

    fun total(): Int {
        return sum
    }

    fun asFinish() : String {
        return when {
            d1 == Dart.NONE -> ""
            d2 == Dart.NONE -> d1.desc()
            d3 == Dart.NONE -> "${d1.desc()} ${d2.desc()}"
            else -> "${d1.desc()} ${d2.desc()} ${d3.desc()}"
        }
    }

    fun lastIsDouble(): Boolean {
        return last().isDouble()
    }

    fun hasZeros(): Boolean{
        return numZeros() > 0
    }

    fun numZeros(): Int{
        var zeros = 0
        if(d1 == Dart.ZERO) zeros++
        if(d2 == Dart.ZERO) zeros++
        if(d3 == Dart.ZERO) zeros++
        return zeros
    }

    fun setDartsUsedForFinish(used: Int) : Turn{
        if(dartsUsed() - numZeros() > used) throw IllegalStateException("Darts Mismatch! used darts($used), but thrown $d1, $d2, $d3")
        return when(used) {
            1 -> Turn(d1)
            2 -> if(d1.isDouble()) Turn(d2, d1) else Turn(d1, d2)
            3 -> if(d1.isDouble()) Turn(d2, d3, d1) else if(d2.isDouble()) Turn(d1, d3, d2) else Turn(d1, d2, d3)
            else -> throw IllegalStateException("Darts Mismatch! used darts($used), but thrown $d1, $d2, $d3")
        }
    }
}