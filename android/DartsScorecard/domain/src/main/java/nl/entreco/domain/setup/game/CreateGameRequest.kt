package nl.entreco.domain.setup.game

/**
 * Created by Entreco on 12/12/2017.
 */
data class CreateGameRequest(val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int)