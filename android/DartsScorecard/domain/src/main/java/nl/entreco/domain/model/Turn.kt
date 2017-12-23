package nl.entreco.domain.model

data class Turn (internal val d1: Dart = Dart.NONE, internal val d2: Dart = Dart.NONE, internal val d3: Dart = Dart.NONE){
    private val sum = d1.value() + d2.value() + d3.value()

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
}