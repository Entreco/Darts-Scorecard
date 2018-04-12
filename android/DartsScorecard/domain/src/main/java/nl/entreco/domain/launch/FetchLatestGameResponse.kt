package nl.entreco.domain.launch

/**
 * Created by Entreco on 19/12/2017.
 */
class FetchLatestGameResponse(val gameId: Long, val teamIds: String, val startScore: Int, val startIndex: Int, val numLegs: Int, val numSets: Int)