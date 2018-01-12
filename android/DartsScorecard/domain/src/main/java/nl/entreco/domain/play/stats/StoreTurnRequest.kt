package nl.entreco.domain.play.stats

import nl.entreco.domain.model.Turn

/**
 * Created by Entreco on 23/12/2017.
 */
data class StoreTurnRequest(val playerId: Long, val gameId: Long, val turn: Turn)