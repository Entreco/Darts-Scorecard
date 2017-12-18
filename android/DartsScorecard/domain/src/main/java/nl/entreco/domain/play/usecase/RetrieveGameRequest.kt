package nl.entreco.domain.play.usecase

import nl.entreco.domain.model.players.TeamIdsString

/**
 * Created by Entreco on 17/12/2017.
 */
data class RetrieveGameRequest(val gameId: Long, val teamIds: TeamIdsString, val settings: GameSettingsRequest)