package nl.entreco.domain.play.stats

import nl.entreco.domain.model.Stat

/**
 * Created by entreco on 16/01/2018.
 */
data class FetchGameStatsResponse(val gameId: Long, val stats: Map<Long, Stat>)