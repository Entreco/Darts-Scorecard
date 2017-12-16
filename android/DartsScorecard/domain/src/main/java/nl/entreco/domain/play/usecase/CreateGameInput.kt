package nl.entreco.domain.play.usecase

/**
 * Created by Entreco on 12/12/2017.
 */
data class CreateGameInput(val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int)