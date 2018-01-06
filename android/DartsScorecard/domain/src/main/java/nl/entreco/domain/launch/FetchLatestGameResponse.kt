package nl.entreco.domain.launch

import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.setup.game.CreateGameRequest

/**
 * Created by Entreco on 19/12/2017.
 */
data class FetchLatestGameResponse(val gameId: Long, val teamIds: TeamIdsString, val request: CreateGameRequest)