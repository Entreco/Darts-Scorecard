package nl.entreco.domain.model

/**
 * Created by entreco on 16/01/2018.
 */
data class Stat(val avg: Double, val n180: Int, val n140: Int, val n100: Int) {
    operator fun plus(stat: Stat?): Stat {
        return this
    }
}