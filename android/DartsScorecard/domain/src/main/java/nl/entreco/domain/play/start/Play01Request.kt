package nl.entreco.domain.play.start

import nl.entreco.domain.repository.TeamIdsString
import nl.entreco.domain.setup.game.CreateGameRequest

/**
 * Created by entreco on 06/01/2018.
 */
data class Play01Request(val gameId: Long, val teamIds: TeamIdsString, val create: CreateGameRequest)