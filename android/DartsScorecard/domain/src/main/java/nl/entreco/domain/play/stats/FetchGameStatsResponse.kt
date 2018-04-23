package nl.entreco.domain.play.stats

import nl.entreco.domain.model.LiveStat

/**
 * Created by entreco on 16/01/2018.
 */
 class FetchGameStatsResponse(val gameId: Long, val stats: Map<Long, LiveStat>)