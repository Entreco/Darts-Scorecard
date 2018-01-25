package nl.entreco.domain.play.start

/**
 * Created by entreco on 06/01/2018.
 */
data class Play01Request(val gameId: Long, val teamIds: String, val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int)