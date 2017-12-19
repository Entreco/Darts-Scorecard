package nl.entreco.domain.launch

import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.TeamIdsString

/**
 * Created by Entreco on 19/12/2017.
 */
data class FetchLatestGameResponse(val gameId: Long, val teamIds: TeamIdsString, val request: CreateGameRequest)