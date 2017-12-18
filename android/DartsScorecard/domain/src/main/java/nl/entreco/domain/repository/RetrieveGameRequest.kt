package nl.entreco.domain.repository

/**
 * Created by Entreco on 17/12/2017.
 */
data class RetrieveGameRequest(val gameId: Long, val teamIds: TeamIdsString, val create: CreateGameRequest)