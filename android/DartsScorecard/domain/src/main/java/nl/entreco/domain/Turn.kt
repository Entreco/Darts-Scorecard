package nl.entreco.domain

data class Turn (val d1: Int, val d2: Int, val d3: Int){
    private val sum = d1 + d2 + d3

    override fun toString(): String {
        return "$sum ($d1,$d2,$d3)"
    }
}