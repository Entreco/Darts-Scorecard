package nl.entreco.domain.setup.game

import nl.entreco.domain.repository.TeamIdsString

/**
 * Created by Entreco on 17/12/2017.
 */
data class CreateGameResponse(val gameId: Long, val teamIds: TeamIdsString, val create: CreateGameRequest)