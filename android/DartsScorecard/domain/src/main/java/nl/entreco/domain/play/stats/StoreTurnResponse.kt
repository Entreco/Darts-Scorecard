package nl.entreco.domain.play.stats

import nl.entreco.domain.model.Turn

/**
 * Created by entreco on 10/01/2018.
 */
data class StoreTurnResponse(val turnId: Long, val turn: Turn)