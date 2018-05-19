package nl.entreco.domain.play.stats

import nl.entreco.domain.model.Turn
import nl.entreco.domain.model.TurnMeta

/**
 * Created by entreco on 10/01/2018.
 */
data class StoreMetaRequest(val turnId: Long, val gameId: Long, val turn: Turn, val turnMeta: TurnMeta)