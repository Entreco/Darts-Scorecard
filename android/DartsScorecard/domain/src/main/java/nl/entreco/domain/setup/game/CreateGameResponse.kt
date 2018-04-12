package nl.entreco.domain.setup.game

/**
 * Created by Entreco on 17/12/2017.
 */
data class CreateGameResponse(val gameId: Long, val teamIds: String, val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int)