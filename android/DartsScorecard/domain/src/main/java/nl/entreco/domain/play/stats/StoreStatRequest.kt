package nl.entreco.domain.play.stats

import nl.entreco.domain.model.Stats

/**
 * Created by entreco on 10/01/2018.
 */
data class StoreStatRequest(val playerId: Long, val turnId: Long, val gameId: Long, val stats: Stats)